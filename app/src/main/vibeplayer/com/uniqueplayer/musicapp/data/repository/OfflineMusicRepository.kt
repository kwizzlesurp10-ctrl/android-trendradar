package com.uniqueplayer.musicapp.data.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.provider.MediaStore
import com.uniqueplayer.musicapp.data.local.dao.MusicDao
import com.uniqueplayer.musicapp.data.local.entity.PlaylistEntity
import com.uniqueplayer.musicapp.data.local.entity.PlaylistTrackCrossRef
import com.uniqueplayer.musicapp.data.local.entity.TrackEntity
import com.uniqueplayer.musicapp.data.mapper.toDomain
import com.uniqueplayer.musicapp.data.mapper.toDomainPlaylist
import com.uniqueplayer.musicapp.domain.model.MusicTrack
import com.uniqueplayer.musicapp.domain.model.Playlist
import com.uniqueplayer.musicapp.domain.repository.MusicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OfflineMusicRepository @Inject constructor(
    private val contentResolver: ContentResolver,
    private val dao: MusicDao
) : MusicRepository {

    override fun observeLibrary(): Flow<List<MusicTrack>> =
        dao.observeTracks().map { list -> list.map(TrackEntity::toDomain) }

    override fun observeQueue(): Flow<List<MusicTrack>> =
        dao.observeQueueTracks(MusicRepository.NOW_PLAYING_QUEUE_ID)
            .map { list -> list.map(TrackEntity::toDomain) }

    override fun observePlaylists(): Flow<List<Playlist>> =
        dao.observePlaylistsWithTracks().map { list -> list.map { it.toDomainPlaylist() } }

    override suspend fun scanDeviceLibrary(): List<MusicTrack> = withContext(Dispatchers.IO) {
        ensureQueuePlaylist()
        val scanned = scanMediaStore()
        if (scanned.isEmpty()) {
            val demo = demoTracks()
            dao.upsertTracks(demo)
            demo.map { it.toDomain() }
        } else {
            dao.upsertTracks(scanned)
            scanned.map { it.toDomain() }
        }
    }

    override suspend fun reorderQueue(trackIds: List<Long>) = withContext(Dispatchers.IO) {
        ensureQueuePlaylist()
        val refs = trackIds.mapIndexed { index, trackId ->
            PlaylistTrackCrossRef(
                playlistId = MusicRepository.NOW_PLAYING_QUEUE_ID,
                trackId = trackId,
                queuePosition = index
            )
        }
        dao.replacePlaylistTracks(MusicRepository.NOW_PLAYING_QUEUE_ID, refs)
    }

    private suspend fun ensureQueuePlaylist() {
        dao.upsertPlaylist(
            PlaylistEntity(
                id = MusicRepository.NOW_PLAYING_QUEUE_ID,
                name = "Now Playing",
                generatedBySmartRecommendation = false
            )
        )
    }

    override suspend fun createPlaylist(name: String, trackIds: List<Long>, smart: Boolean): Playlist =
        withContext(Dispatchers.IO) {
            val playlistId = dao.insertPlaylist(
                PlaylistEntity(name = name, generatedBySmartRecommendation = smart)
            )
            val refs = trackIds.mapIndexed { index, trackId ->
                PlaylistTrackCrossRef(
                    playlistId = playlistId,
                    trackId = trackId,
                    queuePosition = index
                )
            }
            dao.replacePlaylistTracks(playlistId, refs)
            Playlist(id = playlistId, name = name, trackIds = trackIds, isSmart = smart)
        }

    override suspend fun updatePlaylistOrder(playlistId: Long, trackIds: List<Long>) =
        withContext(Dispatchers.IO) {
            val refs = trackIds.mapIndexed { index, trackId ->
                PlaylistTrackCrossRef(
                    playlistId = playlistId,
                    trackId = trackId,
                    queuePosition = index
                )
            }
            dao.replacePlaylistTracks(playlistId, refs)
        }

    override suspend fun getSmartRecommendations(seedTrackId: Long?): List<MusicTrack> =
        withContext(Dispatchers.IO) {
            val library = dao.getTracksSnapshot().map { it.toDomain() }
            if (library.isEmpty()) {
                return@withContext emptyList()
            }
            val seed = seedTrackId?.let { id -> library.firstOrNull { it.id == id } }
            val scored = library
                .filter { track -> track.id != seedTrackId }
                .sortedByDescending { track ->
                    if (seed == null) {
                        track.recommendationScore
                    } else {
                        similarity(track, seed) + track.recommendationScore
                    }
                }
            scored.take(8)
        }

    private fun scanMediaStore(): List<TrackEntity> {
        val supported = setOf(
            "audio/mpeg",
            "audio/mp3",
            "audio/flac",
            "audio/wav",
            "audio/x-wav",
            "audio/aac",
            "audio/mp4",
            "audio/x-m4a"
        )
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.MIME_TYPE,
            MediaStore.Audio.Media.TITLE
        )
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val sort = "${MediaStore.Audio.Media.DATE_ADDED} DESC"
        val tracks = mutableListOf<TrackEntity>()
        contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            sort
        )?.use { cursor ->
            val idIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val nameIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val artistIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val durationIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val mimeIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)
            val titleIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            while (cursor.moveToNext()) {
                val mimeType = cursor.getString(mimeIndex).orEmpty().lowercase(Locale.US)
                if (mimeType.isNotBlank() && mimeType !in supported) {
                    continue
                }
                val id = cursor.getLong(idIndex)
                val uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                ).toString()
                val displayName = cursor.getString(nameIndex).orEmpty()
                val title = cursor.getString(titleIndex)?.takeIf { it.isNotBlank() } ?: displayName
                val artist = cursor.getString(artistIndex).orEmpty().ifBlank { "Unknown Artist" }
                val album = cursor.getString(albumIndex).orEmpty().ifBlank { "Unknown Album" }
                tracks += TrackEntity(
                    id = id,
                    title = title,
                    artist = artist,
                    album = album,
                    uri = uri,
                    durationMs = cursor.getLong(durationIndex).coerceAtLeast(0L),
                    format = mimeType.ifBlank { "audio/mpeg" },
                    lyricsLrc = demoLyricsFor(title),
                    recommendationScore = ((artist.length + album.length) % 10) / 10f
                )
            }
        }
        return tracks
    }

    private fun demoTracks(): List<TrackEntity> = listOf(
        TrackEntity(
            id = 1L,
            title = "Neon Heartbeat",
            artist = "VibePlayer Originals",
            album = "City Lights",
            uri = "content://media/external/audio/media/1",
            durationMs = 198_000,
            format = "audio/mpeg",
            lyricsLrc = demoLyricsFor("Neon Heartbeat"),
            recommendationScore = 0.95f
        ),
        TrackEntity(
            id = 2L,
            title = "Ocean Pulse",
            artist = "VibePlayer Originals",
            album = "City Lights",
            uri = "content://media/external/audio/media/2",
            durationMs = 214_000,
            format = "audio/flac",
            lyricsLrc = demoLyricsFor("Ocean Pulse"),
            recommendationScore = 0.9f
        ),
        TrackEntity(
            id = 3L,
            title = "Afterglow",
            artist = "Night Arcade",
            album = "Spectrum",
            uri = "content://media/external/audio/media/3",
            durationMs = 187_000,
            format = "audio/wav",
            lyricsLrc = demoLyricsFor("Afterglow"),
            recommendationScore = 0.84f
        ),
        TrackEntity(
            id = 4L,
            title = "Skyline Drift",
            artist = "Night Arcade",
            album = "Spectrum",
            uri = "content://media/external/audio/media/4",
            durationMs = 205_000,
            format = "audio/aac",
            lyricsLrc = demoLyricsFor("Skyline Drift"),
            recommendationScore = 0.8f
        )
    )

    private fun demoLyricsFor(title: String): String = buildString {
        appendLine("[00:00.00]$title")
        appendLine("[00:10.00]Bassline wakes the city glow")
        appendLine("[00:20.00]Hands up high, we ride the flow")
        appendLine("[00:30.00]Every beat cuts through the night")
        appendLine("[00:40.00]Vibes align in neon light")
    }

    private fun similarity(track: MusicTrack, seed: MusicTrack): Float {
        var score = 0f
        if (track.artist == seed.artist) score += 1f
        if (track.album == seed.album) score += 0.8f
        if (track.format == seed.format) score += 0.4f
        val durationDelta = kotlin.math.abs(track.durationMs - seed.durationMs)
        score += 1f - (durationDelta / 240_000f).coerceIn(0f, 1f)
        return score
    }
}
