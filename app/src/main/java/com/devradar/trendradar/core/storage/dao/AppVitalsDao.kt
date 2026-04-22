package com.devradar.trendradar.core.storage.dao
import androidx.room.*; import com.devradar.trendradar.core.storage.entity.AppVitalsEntity; import kotlinx.coroutines.flow.Flow
@Dao interface AppVitalsDao {
    @Insert(onConflict=OnConflictStrategy.REPLACE) suspend fun insertAll(items: List<AppVitalsEntity>)
    @Query("SELECT * FROM app_vitals WHERE packageName=:p ORDER BY timestamp DESC LIMIT 1") fun latestForApp(p: String): Flow<AppVitalsEntity?>
}
