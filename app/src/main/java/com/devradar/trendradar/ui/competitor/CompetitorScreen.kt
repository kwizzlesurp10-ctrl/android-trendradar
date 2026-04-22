package com.devradar.trendradar.ui.competitor
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.devradar.trendradar.ui.components.SpiderRadarChart
@Composable
fun CompetitorScreen() {
  Scaffold(topBar={TopAppBar(title={Text("Competitor Radar")})}) { padding ->
    LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp),verticalArrangement=Arrangement.spacedBy(16.dp)) {
      item {
        Text("Comparison Radar",style=MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        SpiderRadarChart(Modifier.fillMaxWidth().height(280.dp),labels=listOf("Rating","Downloads","Revenue","Reviews","Updates","Support"),datasets=listOf(listOf(0.90f,0.85f,0.78f,0.92f,0.70f,0.88f) to Color(0xFF2196F3),listOf(0.75f,0.92f,0.88f,0.70f,0.85f,0.65f) to Color(0xFFF44336),listOf(0.80f,0.70f,0.65f,0.78f,0.90f,0.72f) to Color(0xFF4CAF50)))
      }
      item {
        Text("Top Competitors",style=MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        listOf("PhotoAI Pro" to "4.8★","SnapEditor" to "4.6★","PicMaster" to "4.5★").forEach{(name,rating)->Card(Modifier.fillMaxWidth().padding(vertical=4.dp)){Row(Modifier.padding(16.dp),horizontalArrangement=Arrangement.SpaceBetween){Text(name,style=MaterialTheme.typography.titleLarge);Text(rating)}}}
      }
    }
  }
}
