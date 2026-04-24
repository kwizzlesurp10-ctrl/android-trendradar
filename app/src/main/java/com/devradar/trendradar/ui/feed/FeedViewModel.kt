package com.devradar.trendradar.ui.feed
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devradar.trendradar.core.repository.NewsDataSource
import com.devradar.trendradar.core.repository.NewsItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
data class FeedItem(val id:String,val title:String,val summary:String,val source:String,val url:String,val publishedAt:Long,val tag:String)
data class FeedState(val isLoading:Boolean=true,val isRefreshing:Boolean=false,val items:List<FeedItem>=emptyList(),val error:String?=null)
@HiltViewModel
class FeedViewModel @Inject constructor(
    private val newsDataSource: NewsDataSource
):ViewModel(){
    private val _state=MutableStateFlow(FeedState())
    val state:StateFlow<FeedState>=_state.asStateFlow()
    init{load()}
    private fun load(isRefreshing:Boolean=false){viewModelScope.launch{
        _state.value=_state.value.copy(isLoading=!isRefreshing,isRefreshing=isRefreshing,error=null)
        val now=System.currentTimeMillis()
        runCatching { newsDataSource.loadTrendingNews() }
            .onSuccess { news ->
                val feedItems=news.mapIndexed { index, item -> item.toFeedItem(index, now) }
                _state.value=FeedState(isLoading=false,isRefreshing=false,items=feedItems,error=null)
            }
            .onFailure { error ->
                _state.value=FeedState(
                    isLoading=false,
                    isRefreshing=false,
                    items=emptyList(),
                    error=error.message?:"Unable to load feed"
                )
            }
    }}
    fun refresh(){load(isRefreshing=true)}

    private fun NewsItem.toFeedItem(index:Int, now:Long):FeedItem{
        val sourceLabel=source.ifBlank { "Unknown source" }
        return FeedItem(
            id=id.ifBlank { "feed-$index" },
            title=title.ifBlank { "Untitled item" },
            summary=summary.ifBlank { "No summary available." },
            source=sourceLabel,
            url=url,
            publishedAt=now-(index+1)*3_600_000L,
            tag=sourceLabel.substringBefore(' ').ifBlank { "General" }
        )
    }
}
