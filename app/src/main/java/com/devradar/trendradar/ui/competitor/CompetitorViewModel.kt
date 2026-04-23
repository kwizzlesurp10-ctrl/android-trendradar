package com.devradar.trendradar.ui.competitor
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
data class CompetitorApp(val packageName:String,val name:String,val rating:Float,val installs:String,val category:String,val trend:String,val score:Int)
data class CompetitorState(val isLoading:Boolean=true,val competitors:List<CompetitorApp>=emptyList(),val error:String?=null)
@HiltViewModel
class CompetitorViewModel @Inject constructor():ViewModel(){
    private val _state=MutableStateFlow(CompetitorState())
    val state:StateFlow<CompetitorState>=_state.asStateFlow()
    init{load()}
    private fun load(){viewModelScope.launch{delay(700);_state.value=CompetitorState(isLoading=false,competitors=listOf(
        CompetitorApp("com.rival.a1","AppMetrica Pro",4.6f,"500K+","Dev Tools","+12%",87),
        CompetitorApp("com.rival.a2","Dev Console+",4.3f,"200K+","Dev Tools","+5%",74),
        CompetitorApp("com.rival.a3","StoreStats",4.1f,"100K+","Analytics","-2%",61),
        CompetitorApp("com.rival.a4","PlayInsights",4.5f,"350K+","Analytics","+18%",82),
        CompetitorApp("com.rival.a5","AppFollow",4.4f,"150K+","Marketing","+8%",79)
    ))}}
    fun refresh(){_state.value=CompetitorState();load()}
}
