package com.devradar.trendradar.ui.feed
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
data class FeedItem(val title: String, val source: String, val summary: String, val time: String)
@Composable
fun FeedScreen() {
  val items=remember{listOf(FeedItem("Gemini 2.0 boosts app retention by 40%","Android Dev Blog","New Gemini 2.0 API brings conversational AI into Android apps","1h ago"),FeedItem("Material You v3 patterns for 2026","Material Design","Latest design system updates with expressive components","3h ago"),FeedItem("Play Console shows real-time crash analytics","Google Play","Live crash data within minutes of occurrence","5h ago"),FeedItem("Kotlin 2.1 brings 30% faster compile times","JetBrains","Latest Kotlin release delivers significant performance improvements","1d ago"),FeedItem("Top monetization strategies 2026","AppBrain","Subscription model outperforms ads 3x in analysis of 10K apps","2d ago"))}
  Scaffold(topBar={TopAppBar(title={Text("Trending Feed")})}) { padding ->
    LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
      items(items.size){i->val item=items[i];Card(Modifier.fillMaxWidth()){Column(Modifier.padding(16.dp)){Text(item.title,style=MaterialTheme.typography.titleLarge);Spacer(Modifier.height(4.dp));Text(item.summary,style=MaterialTheme.typography.bodyLarge,maxLines=2);Spacer(Modifier.height(8.dp));Row(horizontalArrangement=Arrangement.SpaceBetween,modifier=Modifier.fillMaxWidth()){Text(item.source,style=MaterialTheme.typography.labelSmall,color=MaterialTheme.colorScheme.primary);Text(item.time,style=MaterialTheme.typography.labelSmall,color=MaterialTheme.colorScheme.onSurfaceVariant)}}}}
    }
  }
}
