package com.devradar.trendradar.core.storage.dao
import androidx.room.*; import com.devradar.trendradar.core.storage.entity.RadarSnapshotEntity; import kotlinx.coroutines.flow.Flow
@Dao interface RadarSnapshotDao {
    @Insert(onConflict=OnConflictStrategy.REPLACE) suspend fun insert(s: RadarSnapshotEntity)
    @Query("SELECT * FROM radar_snapshots WHERE packageName=:p ORDER BY createdAt DESC") fun snapshotsForApp(p: String): Flow<List<RadarSnapshotEntity>>
}
