package com.devradar.trendradar.ui.dashboard
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devradar.trendradar.core.storage.entity.AppVitalsEntity
import com.devradar.trendradar.core.storage.dao.AppVitalsDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
data class DashboardUiState(val apps: List<AppVitalsEntity>=emptyList(), val isLoading: Boolean=false, val error: String?=null)
@HiltViewModel
class DashboardViewModel @Inject constructor(private val dao: AppVitalsDao) : ViewModel() {
  private val _state = MutableStateFlow(DashboardUiState(isLoading=true))
  val state: StateFlow<DashboardUiState> = _state.asStateFlow()
  init { viewModelScope.launch { dao.getAllApps().collect { apps -> _state.value = DashboardUiState(apps=apps) } } }
}
