package com.devradar.trendradar.core.di
import android.content.Context
import androidx.room.Room
import com.devradar.trendradar.core.network.*
import com.devradar.trendradar.core.preferences.UserPreferences
import com.devradar.trendradar.core.repository.*
import com.devradar.trendradar.core.storage.TrendRadarDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module; import dagger.Provides; import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient; import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit; import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton
@Module @InstallIn(SingletonComponent::class)
object AppModule {
    @Provides @Singleton fun provideAuth() = FirebaseAuth.getInstance()
    @Provides @Singleton fun provideFirestore() = FirebaseFirestore.getInstance()
    @Provides @Singleton fun provideOkHttp(): OkHttpClient = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }).build()
    @Provides @Singleton fun providePlayApi(c: OkHttpClient): PlayReportingApiService = Retrofit.Builder().baseUrl("https://playdeveloperreporting.googleapis.com/").addConverterFactory(MoshiConverterFactory.create()).client(c).build().create(PlayReportingApiService::class.java)
    @Provides @Singleton fun provideGemini(c: OkHttpClient): GeminiApiService = GeminiApiService.create(c)
    @Provides @Singleton fun provideDb(@ApplicationContext ctx: Context): TrendRadarDatabase = Room.databaseBuilder(ctx, TrendRadarDatabase::class.java, "trend_radar.db").fallbackToDestructiveMigration().build()
    @Provides @Singleton fun providePrefs(@ApplicationContext ctx: Context) = UserPreferences(ctx)
    @Provides @Singleton fun provideRadarRepo(db: TrendRadarDatabase, fs: FirebaseFirestore) = RadarRepository(db, fs)
    @Provides @Singleton fun provideAppsRepo(api: PlayReportingApiService, fs: FirebaseFirestore) = AppsRepository(api, fs)
    @Provides @Singleton fun provideEcoRepo(fs: FirebaseFirestore) = EcosystemTrendsRepository(fs)
    @Provides @Singleton fun provideInsightsRepo(g: GeminiApiService) = InsightsRepository(g)
    @Provides @Singleton fun provideNewsRepo(fs: FirebaseFirestore, g: GeminiApiService): NewsDataSource = NewsRepository(fs, g)
    @Provides @Singleton fun provideSubRepo() = SubscriptionRepository()
}
