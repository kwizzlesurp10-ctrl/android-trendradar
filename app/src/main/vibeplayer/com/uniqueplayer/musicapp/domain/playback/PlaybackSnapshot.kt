package com.uniqueplayer.musicapp.domain.playback

import com.uniqueplayer.musicapp.domain.model.PlaybackMode

data class PlaybackSnapshot(
    val queueIds: List<Long> = emptyList(),
    val currentTrackId: Long? = null,
    val isPlaying: Boolean = false,
    val positionMs: Long = 0L,
    val mode: PlaybackMode = PlaybackMode.NORMAL,
    val volume: Float = 1f,
    val equalizerBandGains: List<Float> = List(10) { 0f }
)
