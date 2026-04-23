package com.devradar.trendradar.core.preferences
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "trendradar_prefs")
@Singleton
class UserPreferences @Inject constructor(@ApplicationContext private val context: Context) {
  companion object {
    val GEMINI_API_KEY    = stringPreferencesKey("gemini_api_key")
    val PLAY_API_KEY      = stringPreferencesKey("play_api_key")
    val DARK_MODE         = booleanPreferencesKey("dark_mode")
    val NOTIFICATIONS_ON  = booleanPreferencesKey("notifications_on")
    val RATING_ALERTS     = booleanPreferencesKey("rating_alerts")
    val CRASH_ALERTS      = booleanPreferencesKey("crash_alerts")
    val WEEKLY_REPORT     = booleanPreferencesKey("weekly_report")
    val ONBOARDING_DONE   = booleanPreferencesKey("onboarding_done")
  }
  val geminiApiKey: Flow<String?> = context.dataStore.data
    .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
    .map { it[GEMINI_API_KEY] }
  val playApiKey: Flow<String?> = context.dataStore.data
    .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
    .map { it[PLAY_API_KEY] }
  val darkMode: Flow<Boolean> = context.dataStore.data
    .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
    .map { it[DARK_MODE] ?: true }
  val notificationsOn: Flow<Boolean> = context.dataStore.data
    .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
    .map { it[NOTIFICATIONS_ON] ?: true }
  val onboardingDone: Flow<Boolean> = context.dataStore.data
    .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
    .map { it[ONBOARDING_DONE] ?: false }
  suspend fun setGeminiApiKey(key: String) = context.dataStore.edit { it[GEMINI_API_KEY] = key }
  suspend fun setPlayApiKey(key: String)   = context.dataStore.edit { it[PLAY_API_KEY] = key }
  suspend fun setDarkMode(on: Boolean)     = context.dataStore.edit { it[DARK_MODE] = on }
  suspend fun setNotifications(on: Boolean)= context.dataStore.edit { it[NOTIFICATIONS_ON] = on }
  suspend fun setOnboardingDone()          = context.dataStore.edit { it[ONBOARDING_DONE] = true }
  suspend fun clearAll()                   = context.dataStore.edit { it.clear() }
}
