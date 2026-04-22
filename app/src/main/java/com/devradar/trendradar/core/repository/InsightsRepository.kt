package com.devradar.trendradar.core.repository
import com.devradar.trendradar.core.network.GeminiApiService
import com.devradar.trendradar.core.network.GeminiContent
import com.devradar.trendradar.core.network.GeminiPart
import com.devradar.trendradar.core.network.GeminiRequest
import com.devradar.trendradar.core.preferences.UserPreferences
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class InsightsRepository @Inject constructor(
  private val api: GeminiApiService,
  private val prefs: UserPreferences
) {
  suspend fun generateInsight(prompt: String): String {
    val apiKey = prefs.geminiApiKey.first() ?: ""
    if (apiKey.isBlank()) return demoInsight(prompt)
    return try {
      val request = GeminiRequest(
        contents = listOf(
          GeminiContent(
            parts = listOf(GeminiPart(
              text = "You are an expert Android app analytics advisor. Answer concisely in 2-3 sentences. User question: $prompt"
            ))
          )
        )
      )
      val response = api.generateContent(apiKey, request)
      response.candidates.firstOrNull()
        ?.content?.parts?.firstOrNull()?.text
        ?: "No response from Gemini."
    } catch (e: Exception) {
      "Error: ${e.message}. Using demo mode."
    }
  }
  private fun demoInsight(prompt: String): String = when {
    prompt.contains("rating", ignoreCase=true) -> "Your app's rating trend shows a 0.2-point improvement over the last 30 days. Focus on responding to 1-star reviews within 24 hours to further improve ratings."
    prompt.contains("crash", ignoreCase=true) -> "Crash rate is at 0.12%, well below the 1% threshold for Play Store featuring. The latest crashes cluster around Android 13 devices with low memory."
    prompt.contains("revenue", ignoreCase=true) -> "Revenue grew 12% month-over-month driven by subscription conversions. Consider an annual plan discount to boost LTV."
    prompt.contains("competitor", ignoreCase=true) -> "Your top competitor PhotoAI Pro has 4.8 stars vs your 4.7. Their advantage is faster load time; optimizing your cold start could close that gap."
    else -> "Based on your current metrics, your app is performing in the top 15% of its category. Focusing on ASO keyword optimization could drive an additional 20% organic installs."
  }
}
