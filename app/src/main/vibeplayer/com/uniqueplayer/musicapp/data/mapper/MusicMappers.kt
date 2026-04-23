package com.uniqueplayer.musicapp.data.mapper

import androidx.media3.common.C
import com.uniqueplayer.musicapp.data.local.entity.PlaylistWithTracks
import com.uniqueplayer.musicapp.data.local.entity.TrackEntity
import com.uniqueplayer.musicapp.domain.model.LyricLine
import com.uniqueplayer.musicapp.domain.model.MusicTrack
import com.uniqueplayer.musicapp.domain.model.Playlist

fun TrackEntity.toDomain(): MusicTrack =
    MusicTrack(
        id = id,
        title = title,
        artist = artist,
        album = album,
        uri = uri,
        durationMs = durationMs,
        format = format,
        lyrics = parseLyrics(lyricsLrc),
        recommendationScore = recommendationScore
    )

fun PlaylistWithTracks.toDomainPlaylist(): Playlist =
    Playlist(
        id = playlist.id,
        name = playlist.name,
        trackIds = tracks.map(TrackEntity::id),
        isSmart = playlist.generatedBySmartRecommendation
    )

private fun parseLyrics(raw: String?): List<LyricLine> {
    if (raw.isNullOrBlank()) {
        return emptyList()
    }
    return raw.lineSequence()
        .mapNotNull { line ->
            val timestampStart = line.indexOf('[')
            val timestampEnd = line.indexOf(']')
            if (timestampStart != 0 || timestampEnd <= 1) {
                return@mapNotNull null
            }
            val timeToken = line.substring(1, timestampEnd)
            val lyricText = line.substring(timestampEnd + 1).trim()
            val millis = parseLrcTime(timeToken)
            if (millis == C.TIME_UNSET || lyricText.isBlank()) {
                null
            } else {
                LyricLine(timestampMs = millis, text = lyricText)
            }
        }
        .sortedBy(LyricLine::timestampMs)
        .toList()
}

private fun parseLrcTime(token: String): Long {
    val minuteSecond = token.split(":")
    if (minuteSecond.size != 2) {
        return C.TIME_UNSET
    }
    val minutes = minuteSecond[0].toLongOrNull() ?: return C.TIME_UNSET
    val secondFraction = minuteSecond[1].split(".")
    if (secondFraction.isEmpty()) {
        return C.TIME_UNSET
    }
    val seconds = secondFraction[0].toLongOrNull() ?: return C.TIME_UNSET
    val hundredths = secondFraction.getOrNull(1)?.padEnd(2, '0')?.take(2)?.toLongOrNull() ?: 0L
    return (minutes * 60_000L) + (seconds * 1_000L) + (hundredths * 10L)
}
