package com.devradar.trendradar.ui.alerts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
enum class AlertType{RATING_DROP,INSTALLS_SPIKE,REVIEW_SURGE,CRASH_RATE,REVENUE_CHANGE}
data class TrendAlert(val id:String,val title:String,val message:String,val type:AlertType,val timestamp:Long)
data class AlertsState(val isLoading:Boolean=true,val alerts:List<TrendAlert>=emptyList(),val error:String?=null)
@HiltViewModel
class AlertsViewModel @Inject constructor():ViewModel(){
    private val _state=MutableStateFlow(AlertsState())
    val state:StateFlow<AlertsState>=_state.asStateFlow()
    init{load()}
    private fun load(){viewModelScope.launch{delay(600);_state.value=AlertsState(isLoading=false,alerts=listOf(
        TrendAlert("1","Rating Dropped","App rating fell below 4.0 - now 3.9",AlertType.RATING_DROP,System.currentTimeMillis()-3600000L),
        TrendAlert("2","Installs Spike","Install rate up 240% in last 24 hours",AlertType.INSTALLS_SPIKE,System.currentTimeMillis()-7200000L),
        TrendAlert("3","Review Surge","50 new reviews in last 6 hours",AlertType.REVIEW_SURGE,System.currentTimeMillis()-10800000L),
        TrendAlert("4","Crash Rate Alert","Crash rate increased to 2.3%",AlertType.CRASH_RATE,System.currentTimeMillis()-86400000L),
        TrendAlert("5","Revenue Change","Revenue up 18% this week",AlertType.REVENUE_CHANGE,System.currentTimeMillis()-172800000L)
    ))}}
    fun dismiss(id:String){_state.value=_state.value.copy(alerts=_state.value.alerts.filter{it.id!=id})}
    fun refresh(){_state.value=AlertsState();load()}
}
