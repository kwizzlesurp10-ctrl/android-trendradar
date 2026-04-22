package com.devradar.trendradar.ui.myapps
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devradar.trendradar.core.repository.AppVitalsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
data class MyAppsUiState(val isSignedIn: Boolean=false, val isLoading: Boolean=false, val error: String?=null)
@HiltViewModel
class MyAppsViewModel @Inject constructor(private val repo: AppVitalsRepository) : ViewModel() {
  private val _state = MutableStateFlow(MyAppsUiState())
  val state: StateFlow<MyAppsUiState> = _state.asStateFlow()
  fun signIn() { _state.value = _state.value.copy(isSignedIn=true) }
  fun signOut() { _state.value = MyAppsUiState() }
  fun syncApps() { viewModelScope.launch { _state.value=_state.value.copy(isLoading=true); try{repo.syncFromPlayConsole()}catch(e:Exception){_state.value=_state.value.copy(error=e.message)}; _state.value=_state.value.copy(isLoading=false) } }
}
