package com.devradar.trendradar.core.storage
import androidx.room.Database; import androidx.room.RoomDatabase
import com.devradar.trendradar.core.storage.dao.*; import com.devradar.trendradar.core.storage.entity.*
@Database(entities=[AppVitalsEntity::class,RadarSnapshotEntity::class], version=1, exportSchema=true)
abstract class TrendRadarDatabase : RoomDatabase() {
    abstract fun appVitalsDao(): AppVitalsDao
    abstract fun radarSnapshotDao(): RadarSnapshotDao
}
