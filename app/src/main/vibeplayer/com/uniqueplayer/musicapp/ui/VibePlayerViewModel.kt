package com.uniqueplayer.musicapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniqueplayer.musicapp.domain.model.MusicTrack
import com.uniqueplayer.musicapp.domain.playback.AudioVisualizerEngine
import com.uniqueplayer.musicapp.domain.playback.EqualizerSettings
import com.uniqueplayer.musicapp.domain.playback.LyricsSyncEngine
import com.uniqueplayer.musicapp.domain.playback.PlayerController
import com.uniqueplayer.musicapp.domain.playback.QueueReorderUseCase
import com.uniqueplayer.musicapp.domain.playback.SleepTimerController
import com.uniqueplayer.musicapp.domain.repository.MusicRepository
import com.uniqueplayer.musicapp.ui.model.VibePlayerUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class VibePlayerViewModel @Inject constructor(
    private val repository: MusicRepository,
    private val playerController: PlayerController,
    private val sleepTimerController: SleepTimerController,
    private val lyricsSyncEngine: LyricsSyncEngine,
    private val visualizerEngine: AudioVisualizerEngine,
    private val queueReorderUseCase: QueueReorderUseCase
) : ViewModel() {

    private val equalizerState = MutableStateFlow(EqualizerSettings.default())
    private val sleepTimerMinutes = MutableStateFlow(20)
    private val sleepTimerEnabled = MutableStateFlow(false)
    private val sleepRemainingMs = MutableStateFlow(0L)
    private val scanningState = MutableStateFlow(false)
    private val recommendationsState = MutableStateFlow<List<MusicTrack>>(emptyList())
    private var playbackTickerJob: Job? = null
    private var sleepTimerJob: Job? = null

    private data class PlaybackState(
        val library: List<MusicTrack>,
        val queue: List<MusicTrack>,
        val playlists: List<com.uniqueplayer.musicapp.domain.model.Playlist>,
        val snapshot: com.uniqueplayer.musicapp.domain.playback.PlaybackSnapshot
    )

    private data class UiControlsState(
        val equalizer: EqualizerSettings,
        val timerMinutes: Int,
        val timerEnabled: Boolean,
        val timerRemainingMs: Long,
        val recommendations: List<MusicTrack>
    )

    private val playbackState: Flow<PlaybackState> = combine(
        repository.observeLibrary(),
        repository.observeQueue(),
        repository.observePlaylists(),
        playerController.observeSnapshot()
    ) { library, queue, playlists, snapshot ->
        PlaybackState(
            library = library,
            queue = queue,
            playlists = playlists,
            snapshot = snapshot
        )
    }

    private val uiControlsState: Flow<UiControlsState> = combine(
        equalizerState,
        sleepTimerMinutes,
        sleepTimerEnabled,
        sleepRemainingMs,
        recommendationsState
    ) { eq, timerMinutes, timerEnabled, timerRemainingMs, recommendations ->
        UiControlsState(
            equalizer = eq,
            timerMinutes = timerMinutes,
            timerEnabled = timerEnabled,
            timerRemainingMs = timerRemainingMs,
            recommendations = recommendations
        )
    }

    val uiState: StateFlow<VibePlayerUiState> = combine(
        playbackState,
        uiControlsState,
        scanningState
    ) { playback, controls, scanning ->
        val resolvedQueue = if (playback.queue.isNotEmpty()) playback.queue else playback.library
        val currentTrack = resolvedQueue.firstOrNull { it.id == playback.snapshot.currentTrackId }
            ?: resolvedQueue.firstOrNull()
        val activeLyric = currentTrack?.let { track ->
            lyricsSyncEngine.findCurrentLyric(track.lyrics, playback.snapshot.positionMs)
        }
        VibePlayerUiState(
            isLoading = false,
            isScanningLibrary = scanning,
            tracks = playback.library,
            queue = resolvedQueue,
            playlists = playback.playlists,
            recommendedTracks = controls.recommendations,
            currentTrack = currentTrack,
            isPlaying = playback.snapshot.isPlaying,
            playbackMode = playback.snapshot.mode,
            positionMs = playback.snapshot.positionMs,
            durationMs = currentTrack?.durationMs ?: 0L,
            activeLyricLine = activeLyric,
            equalizerBandGains = controls.equalizer.bandGains,
            visualizerSamples = visualizerEngine.generateFrame(
                positionMs = playback.snapshot.positionMs,
                playing = playback.snapshot.isPlaying
            ),
            sleepTimerEnabled = controls.timerEnabled,
            sleepTimerMinutes = controls.timerMinutes,
            sleepTimerRemainingMs = controls.timerRemainingMs
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = VibePlayerUiState()
    )

    init {
        scanLibrary()
        startPlaybackTicker()
    }

    fun scanLibrary() {
        viewModelScope.launch {
            scanningState.update { true }
            val tracks = repository.scanDeviceLibrary()
            if (tracks.isNotEmpty()) {
                val ids = tracks.map { it.id }
                repository.reorderQueue(ids)
                playerController.setQueue(ids)
            }
            scanningState.update { false }
            refreshRecommendations()
        }
    }

    fun togglePlayPause() {
        playerController.togglePlayPause()
    }

    fun playTrack(track: MusicTrack) {
        viewModelScope.launch {
            val currentIds = uiState.value.queue.map { it.id }
            if (track.id !in currentIds) {
                val updatedIds = (currentIds + track.id).distinct()
                repository.reorderQueue(updatedIds)
                playerController.setQueue(updatedIds)
            }
            playerController.playTrack(track.id)
            refreshRecommendations()
        }
    }

    fun skipNext() {
        playerController.skipToNext()
    }

    fun skipPrevious() {
        playerController.skipToPrevious()
    }

    fun moveQueueTrack(fromIndex: Int, toIndex: Int) {
        viewModelScope.launch {
            val reordered = queueReorderUseCase.reorder(uiState.value.queue, fromIndex, toIndex)
            val ids = reordered.map { it.id }
            repository.reorderQueue(ids)
            playerController.setQueue(ids)
        }
    }

    fun setEqualizerBand(index: Int, value: Float) {
        val updated = equalizerState.value.withBandValue(index, value)
        equalizerState.value = updated
        playerController.setEqualizer(updated)
    }

    fun setSleepTimer(minutes: Int) {
        val boundedMinutes = minutes.coerceIn(1, 120)
        sleepTimerMinutes.value = boundedMinutes
        sleepTimerEnabled.value = true
        sleepTimerJob?.cancel()
        sleepTimerController.start(durationMs = boundedMinutes * 60_000L)
        sleepTimerJob = viewModelScope.launch {
            while (sleepTimerController.isActive()) {
                val now = System.currentTimeMillis()
                val progress = sleepTimerController.progress(now)
                val remaining = sleepTimerController.remainingMs(now)
                sleepRemainingMs.value = remaining
                playerController.setVolume(sleepTimerController.volumeForProgress(progress))
                if (remaining <= 0L) {
                    playerController.pause()
                    playerController.setVolume(1f)
                    sleepTimerEnabled.value = false
                    break
                }
                delay(1_000L)
            }
            sleepRemainingMs.value = 0L
        }
    }

    fun cancelSleepTimer() {
        sleepTimerJob?.cancel()
        sleepTimerController.cancel()
        sleepTimerEnabled.value = false
        sleepRemainingMs.value = 0L
        playerController.setVolume(1f)
    }

    fun createSmartPlaylist() {
        viewModelScope.launch {
            val tracks = repository.getSmartRecommendations(uiState.value.currentTrack?.id)
            if (tracks.isNotEmpty()) {
                repository.createPlaylist(
                    name = "Smart Vibes",
                    trackIds = tracks.map { it.id },
                    smart = true
                )
            }
            recommendationsState.value = tracks
        }
    }

    private fun startPlaybackTicker() {
        playbackTickerJob?.cancel()
        playbackTickerJob = viewModelScope.launch {
            while (true) {
                playerController.refreshPosition()
                delay(350L)
            }
        }
    }

    private suspend fun refreshRecommendations() {
        recommendationsState.value = repository.getSmartRecommendations(uiState.value.currentTrack?.id)
    }

    override fun onCleared() {
        playbackTickerJob?.cancel()
        sleepTimerJob?.cancel()
        playerController.release()
        super.onCleared()
    }
}
