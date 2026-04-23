package com.uniqueplayer.musicapp.domain.repository

import com.uniqueplayer.musicapp.domain.model.MusicTrack
import com.uniqueplayer.musicapp.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface MusicRepository {
    fun observeLibrary(): Flow<List<MusicTrack>>
    fun observeQueue(): Flow<List<MusicTrack>>
    fun observePlaylists(): Flow<List<Playlist>>
    suspend fun scanDeviceLibrary(): List<MusicTrack>
    suspend fun reorderQueue(trackIds: List<Long>)
    suspend fun createPlaylist(name: String, trackIds: List<Long>, smart: Boolean): Playlist
    suspend fun updatePlaylistOrder(playlistId: Long, trackIds: List<Long>)
    suspend fun getSmartRecommendations(seedTrackId: Long?): List<MusicTrack>

    companion object {
        const val NOW_PLAYING_QUEUE_ID: Long = 1L
    }
}
