package com.devradar.trendradar.ui.billing
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
data class PlanFeature(val label:String,val free:Boolean,val pro:Boolean)
data class BillingState(val isLoading:Boolean=true,val isPro:Boolean=false,val monthlyPrice:String="\$4.99",val annualPrice:String="\$39.99",val features:List<PlanFeature>=emptyList(),val purchaseSuccess:Boolean=false,val error:String?=null)
@HiltViewModel
class BillingViewModel @Inject constructor():ViewModel(){
    private val _state=MutableStateFlow(BillingState())
    val state:StateFlow<BillingState>=_state.asStateFlow()
    init{viewModelScope.launch{delay(500);_state.value=BillingState(isLoading=false,features=listOf(
        PlanFeature("Radar Dashboard",true,true),
        PlanFeature("My Apps Hub",true,true),
        PlanFeature("Ecosystem Trends",true,true),
        PlanFeature("AI Insights (Gemini)",false,true),
        PlanFeature("Competitor Radar",false,true),
        PlanFeature("Custom Alerts",false,true),
        PlanFeature("Export Reports",false,true),
        PlanFeature("Priority Support",false,true)
    ))}}
    fun purchasePro(annual:Boolean){viewModelScope.launch{delay(1500);_state.value=_state.value.copy(isPro=true,purchaseSuccess=true)}}
}
