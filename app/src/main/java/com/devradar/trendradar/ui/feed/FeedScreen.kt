package com.devradar.trendradar.ui.feed
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
@Composable
fun FeedScreen(vm: FeedViewModel = hiltViewModel()) {
  val state by vm.state.collectAsState()
  Scaffold(topBar={TopAppBar(title={Text("Trending Feed")})}) { padding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .padding(16.dp)
    ) {
      when {
        state.isLoading -> {
          CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        state.error != null -> {
          Text(
            text = state.error ?: "Unable to load feed",
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.bodyLarge
          )
        }
        else -> {
          LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            items(state.items, key = { it.id }) { item ->
              Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                  Text(item.title, style = MaterialTheme.typography.titleLarge)
                  Spacer(Modifier.height(4.dp))
                  Text(item.summary, style = MaterialTheme.typography.bodyLarge, maxLines = 2)
                  Spacer(Modifier.height(8.dp))
                  Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                  ) {
                    Text(
                      item.source,
                      style = MaterialTheme.typography.labelSmall,
                      color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                      item.tag,
                      style = MaterialTheme.typography.labelSmall,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
