package com.devradar.trendradar.core.network
import retrofit2.http.GET; import retrofit2.http.Header; import retrofit2.http.Path
interface PlayReportingApiService {
    @GET("v1beta1/apps/{pkg}/crashRateMetricSet:errorCountDimensions")
    suspend fun getCrashRate(@Header("Authorization") token: String, @Path("pkg") pkg: String): String
}
