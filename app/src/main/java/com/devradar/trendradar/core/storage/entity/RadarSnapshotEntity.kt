package com.devradar.trendradar.core.storage.entity
import androidx.room.Entity; import androidx.room.PrimaryKey
@Entity(tableName="radar_snapshots")
data class RadarSnapshotEntity(@PrimaryKey(autoGenerate=true) val id: Long=0, val packageName: String, val type: String, val metricsJson: String, val createdAt: Long)
