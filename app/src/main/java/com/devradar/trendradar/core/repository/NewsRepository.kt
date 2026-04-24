package com.devradar.trendradar.core.repository
import com.devradar.trendradar.core.network.GeminiApiService
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
data class NewsItem(val id: String, val title: String, val source: String, val url: String, val summary: String)
interface NewsDataSource {
    suspend fun loadTrendingNews(): List<NewsItem>
}
class NewsRepository(private val fs: FirebaseFirestore, val g: GeminiApiService) : NewsDataSource {
    override suspend fun loadTrendingNews(): List<NewsItem> = try {
        fs.collection("news").get().await().documents.map {
            NewsItem(it.id, it.getString("title") ?: "", it.getString("source") ?: "", it.getString("url") ?: "", it.getString("summary") ?: "")
        }
    } catch (e: Exception) { demo() }
    private fun demo() = listOf(
        NewsItem("1","Jetpack Compose 2.0 Released","Android Developers","https://developer.android.com","Major update with improved performance and new Material 3 components."),
        NewsItem("2","Gemini Nano now on more devices","Google Blog","https://blog.google","On-device AI expands to mid-range Android lineup."),
        NewsItem("3","Play Billing v7 migration guide","Android Dev Blog","https://developer.android.com","Step-by-step guide for upgrading to the latest billing API.")
    )
}
