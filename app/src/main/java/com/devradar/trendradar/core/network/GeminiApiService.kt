package com.devradar.trendradar.core.network
import okhttp3.*; import retrofit2.Retrofit; import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body; import retrofit2.http.Headers; import retrofit2.http.POST
interface GeminiApiService {
    @POST("v1beta/models/gemini-1.5-pro:generateContent") @Headers("Content-Type: application/json")
    suspend fun generateContent(@Body body: String): String
    companion object {
        fun create(c: OkHttpClient): GeminiApiService {
            val k = System.getenv("GEMINI_API_KEY") ?: ""
            return Retrofit.Builder().baseUrl("https://generativelanguage.googleapis.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(c.newBuilder().addInterceptor { chain -> chain.proceed(chain.request().newBuilder().url(chain.request().url.newBuilder().addQueryParameter("key", k).build()).build()) }.build())
                .build().create(GeminiApiService::class.java)
        }
    }
}
