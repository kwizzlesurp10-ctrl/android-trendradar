package com.devradar.trendradar.ui.reviews
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.pullrefresh.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devradar.trendradar.ui.components.ShimmerList
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReviewsScreen(vm: ReviewsViewModel = hiltViewModel()) {
  val state by vm.state.collectAsState()
  val pullState = rememberPullRefreshState(refreshing=state.isRefreshing, onRefresh=vm::refresh)
  Scaffold(topBar = { TopAppBar(title={Text("Reviews")}, actions={
    Surface(color=MaterialTheme.colorScheme.primaryContainer, shape=MaterialTheme.shapes.small) {
      Text(" ${state.reviews.size} total ", modifier=Modifier.padding(horizontal=8.dp,vertical=4.dp), style=MaterialTheme.typography.labelSmall)
    }
  })}) { padding ->
    Box(Modifier.fillMaxSize().padding(padding).pullRefresh(pullState)) {
      Column {
        LazyRow(Modifier.padding(horizontal=16.dp,vertical=8.dp), horizontalArrangement=Arrangement.spacedBy(8.dp)) {
          item {
            FilterChip(selected=state.filterRating==null, onClick={vm.setFilter(null)}, label={Text("All")})
          }
          items((5 downTo 1).toList()) { stars ->
            FilterChip(
              selected=state.filterRating==stars,
              onClick={vm.setFilter(if(state.filterRating==stars) null else stars)},
              label={Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(2.dp)){Text("$stars");Icon(Icons.Filled.Star,null,Modifier.size(12.dp),tint=Color(0xFFFFC107))}}
            )
          }
        }
        if (state.isLoading) { ShimmerList(modifier=Modifier.padding(horizontal=16.dp)) }
        else {
          LazyColumn(Modifier.fillMaxSize().padding(horizontal=16.dp), verticalArrangement=Arrangement.spacedBy(12.dp)) {
            items(state.reviews, key={it.id}) { review ->
              ReviewCard(review)
            }
            item { Spacer(Modifier.height(16.dp)) }
          }
        }
      }
      PullRefreshIndicator(refreshing=state.isRefreshing, state=pullState, modifier=Modifier.align(Alignment.TopCenter))
    }
  }
}
@Composable
fun ReviewCard(review: AppReview) {
  val sentimentColor = when {
    review.rating >= 4 -> Color(0xFF4CAF50)
    review.rating == 3 -> Color(0xFFFFC107)
    else               -> Color(0xFFF44336)
  }
  Card(Modifier.fillMaxWidth(), border=androidx.compose.foundation.BorderStroke(1.dp, sentimentColor.copy(alpha=0.3f))) {
    Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.spacedBy(8.dp)) {
      Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween, verticalAlignment=Alignment.CenterVertically) {
        Text(review.author, style=MaterialTheme.typography.titleLarge)
        Row(horizontalArrangement=Arrangement.spacedBy(2.dp)) {
          (1..5).forEach { i -> Icon(if(i<=review.rating) Icons.Filled.Star else Icons.Outlined.StarOutline, null, Modifier.size(16.dp), tint=if(i<=review.rating) Color(0xFFFFC107) else MaterialTheme.colorScheme.onSurfaceVariant) }
        }
      }
      Text(review.text, style=MaterialTheme.typography.bodyLarge)
      Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween, verticalAlignment=Alignment.CenterVertically) {
        Text("${review.date} • v${review.version}", style=MaterialTheme.typography.labelSmall, color=MaterialTheme.colorScheme.onSurfaceVariant)
        if (review.replied) Surface(color=MaterialTheme.colorScheme.secondaryContainer, shape=MaterialTheme.shapes.small) { Text(" Replied ", style=MaterialTheme.typography.labelSmall, modifier=Modifier.padding(horizontal=6.dp,vertical=2.dp)) }
        else TextButton(onClick={}, contentPadding=PaddingValues(horizontal=8.dp,vertical=2.dp)) { Text("Reply", style=MaterialTheme.typography.labelSmall) }
      }
    }
  }
}
