package com.devradar.trendradar.ui.alerts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
data class Alert(val title: String, val message: String, val time: String, val severity: String)
@Composable
fun AlertsScreen() {
  val alerts=remember{listOf(Alert("Rating Drop","App rating dropped from 4.7 to 4.5","2h ago","high"),Alert("New Competitor","PhotoAI Pro launched in your category","5h ago","medium"),Alert("Crash Spike","Crash rate increased by 0.05%","1d ago","high"),Alert("Revenue Milestone","Reached \$10K monthly revenue!","2d ago","info"),Alert("Review Response","3 unanswered reviews need attention","3d ago","medium"))}
  Scaffold(topBar={TopAppBar(title={Text("Alerts & Reports")})}) { padding ->
    LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
      items(alerts.size){i->val a=alerts[i];val c=when(a.severity){"high"->MaterialTheme.colorScheme.error;"medium"->MaterialTheme.colorScheme.tertiary;else->MaterialTheme.colorScheme.primary};Card(Modifier.fillMaxWidth()){Row(Modifier.padding(16.dp),horizontalArrangement=Arrangement.spacedBy(12.dp)){Icon(Icons.Default.Notifications,null,tint=c);Column{Text(a.title,style=MaterialTheme.typography.titleLarge);Text(a.message,style=MaterialTheme.typography.bodyLarge);Text(a.time,style=MaterialTheme.typography.labelSmall,color=MaterialTheme.colorScheme.onSurfaceVariant)}}}}
    }
  }
}
