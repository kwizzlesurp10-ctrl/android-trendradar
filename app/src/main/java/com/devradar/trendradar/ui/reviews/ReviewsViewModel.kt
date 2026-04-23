package com.devradar.trendradar.ui.reviews
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
data class AppReview(
  val id: String, val author: String, val rating: Int, val text: String,
  val date: String, val version: String, val replied: Boolean = false
)
data class ReviewsUiState(
  val reviews: List<AppReview> = emptyList(), val isLoading: Boolean = false,
  val isRefreshing: Boolean = false, val filterRating: Int? = null, val error: String? = null
)
@HiltViewModel
class ReviewsViewModel @Inject constructor() : ViewModel() {
  private val _all = listOf(
    AppReview("r1","Sarah K.",5,"Best analytics app I've used! The radar chart is incredible.","Apr 20","1.0"),
    AppReview("r2","DevMike",4,"Really useful for tracking competitors. Would love more chart types.","Apr 18","1.0"),
    AppReview("r3","AppBuilder99",3,"Good concept but Gemini insights are sometimes slow to load.","Apr 15","1.0"),
    AppReview("r4","Priya R.",5,"Exactly what I needed as an indie dev. Alerts saved me from a crash crisis!","Apr 12","1.0"),
    AppReview("r5","JohnDev",2,"App crashed on my Pixel 9 twice. Please fix stability.","Apr 10","1.0",replied=true),
    AppReview("r6","TechLaura",5,"The competitor radar is a game changer. Worth every penny.","Apr 8","1.0"),
    AppReview("r7","MobileDev42",4,"Clean UI. The trending feed keeps me up to date on Android news.","Apr 5","1.0"),
    AppReview("r8","StartupSam",1,"Paid for Pro but AI insights aren't working. Need refund option.","Apr 3","1.0")
  )
  private val _state = MutableStateFlow(ReviewsUiState(isLoading=true))
  val state: StateFlow<ReviewsUiState> = _state.asStateFlow()
  init { viewModelScope.launch { delay(600); _state.value = ReviewsUiState(reviews=_all) } }
  fun setFilter(rating: Int?) { _state.value = _state.value.copy(filterRating=rating, reviews=if(rating==null)_all else _all.filter{it.rating==rating}) }
  fun refresh() {
    viewModelScope.launch {
      _state.value = _state.value.copy(isRefreshing=true)
      delay(800)
      _state.value = _state.value.copy(isRefreshing=false, reviews=if(_state.value.filterRating==null)_all else _all.filter{it.rating==_state.value.filterRating})
    }
  }
}
