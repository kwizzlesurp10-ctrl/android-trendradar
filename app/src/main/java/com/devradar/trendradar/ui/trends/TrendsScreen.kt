package com.devradar.trendradar.ui.trends
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
fun TrendsScreen(vm: TrendsViewModel=hiltViewModel()) {
  val state by vm.state.collectAsState()
  Scaffold(topBar={TopAppBar(title={Text("Ecosystem Trends")})}) { padding ->
    LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
      items(state.trends) { trend ->
        Card(Modifier.fillMaxWidth()) {
          Row(Modifier.padding(16.dp),horizontalArrangement=Arrangement.SpaceBetween) {
            Column{Text(trend.name,style=MaterialTheme.typography.titleLarge);Text(trend.category,style=MaterialTheme.typography.bodyLarge,color=MaterialTheme.colorScheme.onSurfaceVariant)}
            Column(horizontalAlignment=Alignment.End){Text("${(trend.score*100).toInt()}%",style=MaterialTheme.typography.titleLarge);Text(trend.delta,style=MaterialTheme.typography.labelSmall,color=MaterialTheme.colorScheme.primary)}
          }
        }
      }
    }
  }
}
