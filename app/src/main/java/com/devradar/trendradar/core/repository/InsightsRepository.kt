package com.devradar.trendradar.core.repository
import com.devradar.trendradar.core.network.GeminiApiService
class InsightsRepository(private val g: GeminiApiService) {
    suspend fun askInsight(prompt: String): String = try {
        val escaped = prompt.replace("\"", "\\\"")
        g.generateContent("""{"contents":[{"parts":[{"text":"$escaped"}]}]}""")
    } catch (e: Exception) { "[Demo] I'd analyze your radar here. Set GEMINI_API_KEY to enable live AI insights." }
}
