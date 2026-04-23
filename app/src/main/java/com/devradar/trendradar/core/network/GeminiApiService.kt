package com.devradar.trendradar.core.network
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
@JsonClass(generateAdapter=true)
data class GeminiPart(@Json(name="text") val text: String)
@JsonClass(generateAdapter=true)
data class GeminiContent(@Json(name="parts") val parts: List<GeminiPart>, @Json(name="role") val role: String="user")
@JsonClass(generateAdapter=true)
data class GeminiConfig(@Json(name="temperature") val temperature: Float=0.7f, @Json(name="maxOutputTokens") val maxTokens: Int=512)
@JsonClass(generateAdapter=true)
data class GeminiRequest(@Json(name="contents") val contents: List<GeminiContent>, @Json(name="generationConfig") val config: GeminiConfig=GeminiConfig())
@JsonClass(generateAdapter=true)
data class GeminiCandidate(@Json(name="content") val content: GeminiContent, @Json(name="finishReason") val finishReason: String?=null)
@JsonClass(generateAdapter=true)
data class GeminiResponse(@Json(name="candidates") val candidates: List<GeminiCandidate>=emptyList())
interface GeminiApiService {
  @POST("v1beta/models/gemini-2.0-flash:generateContent")
  suspend fun generateContent(@Query("key") apiKey: String, @Body request: GeminiRequest): GeminiResponse
}
