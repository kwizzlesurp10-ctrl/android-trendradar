package com.uniqueplayer.musicapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.uniqueplayer.musicapp.data.local.dao.MusicDao
import com.uniqueplayer.musicapp.data.local.entity.PlaylistEntity
import com.uniqueplayer.musicapp.data.local.entity.PlaylistTrackCrossRef
import com.uniqueplayer.musicapp.data.local.entity.TrackEntity

@Database(
    entities = [TrackEntity::class, PlaylistEntity::class, PlaylistTrackCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class VibePlayerDatabase : RoomDatabase() {
    abstract fun musicDao(): MusicDao
}
