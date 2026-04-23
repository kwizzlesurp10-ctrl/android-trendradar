package com.uniqueplayer.musicapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.uniqueplayer.musicapp.data.local.entity.PlaylistEntity
import com.uniqueplayer.musicapp.data.local.entity.PlaylistTrackCrossRef
import com.uniqueplayer.musicapp.data.local.entity.PlaylistWithTracks
import com.uniqueplayer.musicapp.data.local.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicDao {
    @Query("SELECT * FROM tracks ORDER BY title COLLATE NOCASE ASC")
    fun observeTracks(): Flow<List<TrackEntity>>

    @Query(
        """
        SELECT t.* FROM tracks t
        INNER JOIN playlist_track_cross_ref q ON q.trackId = t.id
        WHERE q.playlistId = :playlistId
        ORDER BY q.queuePosition ASC
        """
    )
    fun observeQueueTracks(playlistId: Long): Flow<List<TrackEntity>>

    @Query("SELECT * FROM tracks")
    suspend fun getTracksSnapshot(): List<TrackEntity>

    @Transaction
    @Query("SELECT * FROM playlists ORDER BY name COLLATE NOCASE ASC")
    fun observePlaylistsWithTracks(): Flow<List<PlaylistWithTracks>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTracks(tracks: List<TrackEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPlaylist(playlist: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPlaylistTracks(crossRefs: List<PlaylistTrackCrossRef>)

    @Query("DELETE FROM playlist_track_cross_ref WHERE playlistId = :playlistId")
    suspend fun clearPlaylistTracks(playlistId: Long)

    @Transaction
    suspend fun replacePlaylistTracks(
        playlistId: Long,
        crossRefs: List<PlaylistTrackCrossRef>
    ) {
        clearPlaylistTracks(playlistId)
        if (crossRefs.isNotEmpty()) {
            upsertPlaylistTracks(crossRefs)
        }
    }
}
