package com.devradar.trendradar.core.repository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
data class EcosystemTrend(val label: String, val value: Float)
class EcosystemTrendsRepository(private val fs: FirebaseFirestore) {
    suspend fun loadPlatformDistribution(): List<EcosystemTrend> = try {
        val s = fs.collection("ecosystem_trends").document("platform_distribution").get().await()
        (s.get("items") as? List<Map<String,Any>>)?.map { EcosystemTrend(it["label"] as String, (it["value"] as Number).toFloat()) } ?: demo()
    } catch (e: Exception) { demo() }
    private fun demo() = listOf(EcosystemTrend("Android 14",0.45f),EcosystemTrend("Android 13",0.30f),EcosystemTrend("Android 12",0.15f),EcosystemTrend("Android 11",0.07f),EcosystemTrend("Android 10",0.03f))
}
