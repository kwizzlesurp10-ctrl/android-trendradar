package com.devradar.trendradar.core.preferences
import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
private val Context.ds by preferencesDataStore("trend_radar_prefs")
class UserPreferences(private val ctx: Context) {
    private val THEME = stringPreferencesKey("theme_mode")
    private val NOTIF = booleanPreferencesKey("notifications")
    private val DEMO = booleanPreferencesKey("demo_mode")
    val themeMode = ctx.ds.data.map { it[THEME] ?: "system" }
    val notificationsEnabled = ctx.ds.data.map { it[NOTIF] ?: true }
    val demoMode = ctx.ds.data.map { it[DEMO] ?: true }
    suspend fun setThemeMode(v: String) = ctx.ds.edit { it[THEME] = v }
    suspend fun setNotificationsEnabled(v: Boolean) = ctx.ds.edit { it[NOTIF] = v }
    suspend fun setDemoMode(v: Boolean) = ctx.ds.edit { it[DEMO] = v }
}
