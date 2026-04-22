package com.devradar.trendradar.ui.dashboard
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devradar.trendradar.ui.components.SpiderRadarChart
import com.devradar.trendradar.ui.components.StatCard
@Composable
fun DashboardScreen(vm: DashboardViewModel=hiltViewModel()) {
  val state by vm.state.collectAsState()
  Scaffold(topBar={TopAppBar(title={Text("Radar Dashboard")})}) { padding ->
    if(state.isLoading) { Box(Modifier.fillMaxSize(),contentAlignment=Alignment.Center){CircularProgressIndicator()} }
    else {
      LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp),verticalArrangement=Arrangement.spacedBy(16.dp)) {
        item {
          Text("Performance Radar",style=MaterialTheme.typography.titleLarge)
          Spacer(Modifier.height(8.dp))
          SpiderRadarChart(Modifier.fillMaxWidth().height(280.dp),labels=listOf("Rating","Reviews","Installs","Crashes","Revenue","DAU"),datasets=listOf(listOf(0.85f,0.72f,0.91f,0.65f,0.78f,0.88f) to MaterialTheme.colorScheme.primary,listOf(0.70f,0.65f,0.75f,0.80f,0.60f,0.70f) to MaterialTheme.colorScheme.secondary))
        }
        item {
          Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(12.dp)){StatCard(Modifier.weight(1f),"Rating","4.7★","+0.2");StatCard(Modifier.weight(1f),"Installs","12.4K","+340")}
          Spacer(Modifier.height(8.dp))
          Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(12.dp)){StatCard(Modifier.weight(1f),"Crashes","0.12%","-0.03%");StatCard(Modifier.weight(1f),"Revenue","\$1,240","+\$120")}
        }
        item{Text("My Apps",style=MaterialTheme.typography.titleLarge)}
        if(state.apps.isEmpty()) item{Text("No apps. Connect Play Console in My Apps Hub.")}
        else items(state.apps){app->Card(Modifier.fillMaxWidth()){Column(Modifier.padding(16.dp)){Text(app.appName,style=MaterialTheme.typography.titleLarge);Row(horizontalArrangement=Arrangement.spacedBy(16.dp)){Text("Rating: ${app.rating}");Text("Installs: ${app.installs}")}}}}
      }
    }
  }
}
