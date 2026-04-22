package com.devradar.trendradar.ui.insights
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devradar.trendradar.core.repository.InsightsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
data class InsightsUiState(val insights: List<String>=emptyList(), val isLoading: Boolean=false, val error: String?=null, val prompt: String="")
@HiltViewModel
class InsightsViewModel @Inject constructor(private val repo: InsightsRepository) : ViewModel() {
  private val _state = MutableStateFlow(InsightsUiState())
  val state: StateFlow<InsightsUiState> = _state.asStateFlow()
  fun onPromptChange(v: String) { _state.value = _state.value.copy(prompt=v) }
  fun generate() { val p=_state.value.prompt.ifBlank{return}; viewModelScope.launch { _state.value=_state.value.copy(isLoading=true,error=null); try{val r=repo.generateInsight(p);_state.value=_state.value.copy(insights=_state.value.insights+r,isLoading=false)}catch(e:Exception){_state.value=_state.value.copy(error=e.message,isLoading=false)} } }
}
