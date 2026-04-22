package com.devradar.trendradar.ui.trends
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
data class Trend(val name: String, val category: String, val score: Float, val delta: String)
data class TrendsUiState(val trends: List<Trend>=emptyList(), val isLoading: Boolean=false)
@HiltViewModel
class TrendsViewModel @Inject constructor() : ViewModel() {
  private val _state = MutableStateFlow(TrendsUiState(isLoading=true))
  val state: StateFlow<TrendsUiState> = _state.asStateFlow()
  init { viewModelScope.launch { _state.value = TrendsUiState(trends=listOf(Trend("AI Photo Editor","Productivity",0.95f,"+12%"),Trend("Fitness Tracker","Health",0.88f,"+8%"),Trend("Budget Planner","Finance",0.82f,"+5%"),Trend("Language Learning","Education",0.79f,"+15%"),Trend("Sleep Tracker","Health",0.74f,"+6%"))) } }
}
