package com.devradar.trendradar.core.repository
import com.devradar.trendradar.core.storage.TrendRadarDatabase
import com.devradar.trendradar.core.storage.entity.RadarSnapshotEntity
import com.google.firebase.firestore.FirebaseFirestore
class RadarRepository(private val db: TrendRadarDatabase, private val fs: FirebaseFirestore) {
    fun observeSnapshots(pkg: String) = db.radarSnapshotDao().snapshotsForApp(pkg)
    suspend fun saveSnapshot(pkg: String, type: String, json: String) {
        val e = RadarSnapshotEntity(packageName=pkg, type=type, metricsJson=json, createdAt=System.currentTimeMillis())
        db.radarSnapshotDao().insert(e)
        fs.collection("radarSnapshots").add(mapOf("packageName" to pkg, "type" to type, "metricsJson" to json))
    }
}
