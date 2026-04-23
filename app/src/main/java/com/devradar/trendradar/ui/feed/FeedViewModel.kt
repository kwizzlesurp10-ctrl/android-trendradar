package com.devradar.trendradar.ui.feed
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
data class FeedItem(val id:String,val title:String,val summary:String,val source:String,val url:String,val publishedAt:Long,val tag:String)
data class FeedState(val isLoading:Boolean=true,val isRefreshing:Boolean=false,val items:List<FeedItem>=emptyList(),val error:String?=null)
@HiltViewModel
class FeedViewModel @Inject constructor():ViewModel(){
    private val _state=MutableStateFlow(FeedState())
    val state:StateFlow<FeedState>=_state.asStateFlow()
    init{load()}
    private fun load(){viewModelScope.launch{delay(800);_state.value=FeedState(isLoading=false,items=listOf(
        FeedItem("1","Android 16 Developer Preview Released","Major changes to background task scheduling and new Compose APIs.","Android Developers Blog","https://android.googleblog.com",System.currentTimeMillis()-3600000L,"Android"),
        FeedItem("2","Play Store Policy Update","Developers must update data safety sections by Q3 2026.","Google Play","https://play.google.com",System.currentTimeMillis()-7200000L,"Policy"),
        FeedItem("3","Kotlin 2.1 K2 Compiler Default","K2 compiler now default in stable Kotlin, bringing 2x faster compilation.","Kotlin Blog","https://blog.kotlin.org",System.currentTimeMillis()-14400000L,"Kotlin"),
        FeedItem("4","Jetpack Compose 1.8 Stable","Lazy grid improvements, shared element transitions, predictive back.","Android Developers","https://developer.android.com",System.currentTimeMillis()-86400000L,"Compose"),
        FeedItem("5","Play Billing Library 7 Migration","Breaking changes in subscription management and new purchase flows.","Google Play","https://play.google.com",System.currentTimeMillis()-172800000L,"Billing")
    ))}}
    fun refresh(){viewModelScope.launch{_state.value=_state.value.copy(isRefreshing=true);delay(1000);load()}}
}
