package com.uniqueplayer.musicapp.ui.model

import com.uniqueplayer.musicapp.domain.model.LyricLine
import com.uniqueplayer.musicapp.domain.model.MusicTrack
import com.uniqueplayer.musicapp.domain.model.PlaybackMode
import com.uniqueplayer.musicapp.domain.model.Playlist

data class VibePlayerUiState(
    val isLoading: Boolean = false,
    val isScanningLibrary: Boolean = false,
    val offlineModeEnabled: Boolean = true,
    val tracks: List<MusicTrack> = emptyList(),
    val queue: List<MusicTrack> = emptyList(),
    val playlists: List<Playlist> = emptyList(),
    val recommendedTracks: List<MusicTrack> = emptyList(),
    val currentTrack: MusicTrack? = null,
    val isPlaying: Boolean = false,
    val playbackMode: PlaybackMode = PlaybackMode.NORMAL,
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
    val activeLyricLine: LyricLine? = null,
    val equalizerBandGains: List<Float> = List(10) { 0f },
    val visualizerSamples: List<Float> = emptyList(),
    val sleepTimerEnabled: Boolean = false,
    val sleepTimerMinutes: Int = 20,
    val sleepTimerRemainingMs: Long = 0L
) {
    val sleepTimerLabel: String
        get() {
            val seconds = sleepTimerRemainingMs / 1_000L
            val minutes = seconds / 60L
            val remainder = seconds % 60L
            return "%02d:%02d remaining".format(minutes, remainder)
        }
}
