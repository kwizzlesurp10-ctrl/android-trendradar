package com.devradar.trendradar.core.storage.entity
import androidx.room.Entity; import androidx.room.PrimaryKey
@Entity(tableName="app_vitals")
data class AppVitalsEntity(@PrimaryKey(autoGenerate=true) val id: Long=0, val packageName: String, val crashRate: Float, val anrRate: Float, val d1Retention: Float, val d7Retention: Float, val d30Retention: Float, val installGrowth: Float, val batteryImpact: Float, val launchTimeMs: Float, val asoScore: Float, val userRating: Float, val timestamp: Long)
