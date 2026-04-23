package com.uniqueplayer.musicapp.domain.model

data class MusicTrack(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val uri: String,
    val durationMs: Long,
    val format: String,
    val lyrics: List<LyricLine> = emptyList(),
    val recommendationScore: Float = 0f
)
