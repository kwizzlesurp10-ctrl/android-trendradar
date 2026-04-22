package com.devradar.trendradar.ui.settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devradar.trendradar.core.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
data class SettingsUiState(
  val darkMode: Boolean = true,
  val notificationsEnabled: Boolean = true,
  val ratingAlertsEnabled: Boolean = true,
  val crashAlertsEnabled: Boolean = true,
  val weeklyReportEnabled: Boolean = false,
  val geminiApiKey: String = "",
  val isSaved: Boolean = false
)
@HiltViewModel
class SettingsViewModel @Inject constructor(private val prefs: UserPreferences) : ViewModel() {
  private val _state = MutableStateFlow(SettingsUiState())
  val state: StateFlow<SettingsUiState> = _state.asStateFlow()
  init {
    viewModelScope.launch {
      prefs.geminiApiKey.collect { key ->
        _state.value = _state.value.copy(geminiApiKey = key ?: "")
      }
    }
  }
  fun toggleDarkMode() { _state.value = _state.value.copy(darkMode = !_state.value.darkMode, isSaved = false) }
  fun toggleNotifications() { _state.value = _state.value.copy(notificationsEnabled = !_state.value.notificationsEnabled, isSaved = false) }
  fun toggleRatingAlerts() { _state.value = _state.value.copy(ratingAlertsEnabled = !_state.value.ratingAlertsEnabled, isSaved = false) }
  fun toggleCrashAlerts() { _state.value = _state.value.copy(crashAlertsEnabled = !_state.value.crashAlertsEnabled, isSaved = false) }
  fun toggleWeeklyReport() { _state.value = _state.value.copy(weeklyReportEnabled = !_state.value.weeklyReportEnabled, isSaved = false) }
  fun onGeminiKeyChange(v: String) { _state.value = _state.value.copy(geminiApiKey = v, isSaved = false) }
  fun save() {
    viewModelScope.launch {
      prefs.setGeminiApiKey(_state.value.geminiApiKey)
      _state.value = _state.value.copy(isSaved = true)
    }
  }
}
