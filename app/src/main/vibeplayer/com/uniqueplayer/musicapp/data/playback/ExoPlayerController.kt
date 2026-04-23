package com.uniqueplayer.musicapp.data.playback

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.uniqueplayer.musicapp.domain.model.PlaybackMode
import com.uniqueplayer.musicapp.domain.playback.EqualizerSettings
import com.uniqueplayer.musicapp.domain.playback.PlaybackSnapshot
import com.uniqueplayer.musicapp.domain.playback.PlayerController
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Singleton
class ExoPlayerController @Inject constructor(
    @ApplicationContext context: Context
) : PlayerController {
    private val exoPlayer = ExoPlayer.Builder(context).build()
    private val snapshots = MutableStateFlow(PlaybackSnapshot())

    private var repeatMode: PlaybackMode = PlaybackMode.NORMAL
    private var equalizerBandGains: List<Float> = List(10) { 0f }

    private val listener = object : Player.Listener {
        override fun onEvents(player: Player, events: Player.Events) {
            publishSnapshot()
        }
    }

    init {
        exoPlayer.addListener(listener)
    }

    override fun observeSnapshot(): StateFlow<PlaybackSnapshot> = snapshots.asStateFlow()

    override fun setQueue(trackIds: List<Long>) {
        val mediaItems = trackIds.map { trackId ->
            MediaItem.Builder()
                .setMediaId(trackId.toString())
                .setUri("content://media/external/audio/media/$trackId")
                .build()
        }
        exoPlayer.setMediaItems(mediaItems, false)
        exoPlayer.prepare()
        publishSnapshot(queueIds = trackIds)
    }

    override fun playTrack(trackId: Long) {
        val targetIndex = exoPlayer.currentTimeline.windowCount
            .let { count -> (0 until count).firstOrNull { exoPlayer.getMediaItemAt(it).mediaId == trackId.toString() } }
            ?: 0
        exoPlayer.seekTo(targetIndex, 0L)
        exoPlayer.playWhenReady = true
        exoPlayer.play()
        publishSnapshot(currentTrackId = trackId)
    }

    override fun togglePlayPause() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
        } else {
            exoPlayer.playWhenReady = true
            exoPlayer.play()
        }
        publishSnapshot()
    }

    override fun skipToNext() {
        if (exoPlayer.hasNextMediaItem()) {
            exoPlayer.seekToNextMediaItem()
            exoPlayer.playWhenReady = true
            publishSnapshot()
        }
    }

    override fun skipToPrevious() {
        if (exoPlayer.hasPreviousMediaItem()) {
            exoPlayer.seekToPreviousMediaItem()
            exoPlayer.playWhenReady = true
            publishSnapshot()
        }
    }

    override fun pause() {
        exoPlayer.pause()
        publishSnapshot()
    }

    override fun setVolume(volume: Float) {
        exoPlayer.volume = volume.coerceIn(0f, 1f)
        publishSnapshot()
    }

    override fun updateMode(mode: PlaybackMode) {
        repeatMode = mode
        when (mode) {
            PlaybackMode.NORMAL -> {
                exoPlayer.repeatMode = Player.REPEAT_MODE_OFF
                exoPlayer.shuffleModeEnabled = false
            }

            PlaybackMode.REPEAT_ONE -> {
                exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
                exoPlayer.shuffleModeEnabled = false
            }

            PlaybackMode.REPEAT_ALL -> {
                exoPlayer.repeatMode = Player.REPEAT_MODE_ALL
                exoPlayer.shuffleModeEnabled = false
            }

            PlaybackMode.SHUFFLE -> {
                exoPlayer.repeatMode = Player.REPEAT_MODE_ALL
                exoPlayer.shuffleModeEnabled = true
            }
        }
        publishSnapshot()
    }

    override fun setEqualizer(settings: EqualizerSettings) {
        equalizerBandGains = settings.bandGains
        publishSnapshot()
    }

    override fun refreshPosition() {
        publishSnapshot()
    }

    private fun publishSnapshot(
        queueIds: List<Long>? = null,
        currentTrackId: Long? = null
    ) {
        val currentQueue = queueIds ?: snapshots.value.queueIds
        val selectedTrackId = currentTrackId
            ?: exoPlayer.currentMediaItem?.mediaId?.toLongOrNull()
            ?: snapshots.value.currentTrackId
        snapshots.value = PlaybackSnapshot(
            queueIds = if (currentQueue.isEmpty()) {
                (0 until exoPlayer.mediaItemCount).mapNotNull { index ->
                    exoPlayer.getMediaItemAt(index).mediaId.toLongOrNull()
                }
            } else {
                currentQueue
            },
            currentTrackId = selectedTrackId,
            isPlaying = exoPlayer.isPlaying,
            positionMs = exoPlayer.currentPosition.coerceAtLeast(0L),
            mode = repeatMode,
            volume = exoPlayer.volume,
            equalizerBandGains = equalizerBandGains
        )
    }

    override fun release() {
        exoPlayer.removeListener(listener)
        exoPlayer.release()
    }
}
