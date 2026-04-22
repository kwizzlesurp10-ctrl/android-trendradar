package com.devradar.trendradar.ui.billing
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
data class PricingPlan(val name: String, val price: String, val period: String, val features: List<String>, val highlighted: Boolean = false)
@Composable
fun BillingScreen(onSubscribe: (String) -> Unit = {}) {
  val plans = listOf(
    PricingPlan("Free", "\$0", "forever", listOf("Radar Dashboard", "5 app limit", "Weekly reports", "Basic trends")),
    PricingPlan("Pro", "\$9.99", "/ month", listOf("Everything in Free", "Unlimited apps", "AI Insights (Gemini)", "Competitor Radar", "Real-time alerts", "Daily reports"), highlighted = true),
    PricingPlan("Studio", "\$24.99", "/ month", listOf("Everything in Pro", "Team access (5 seats)", "API access", "White-label reports", "Priority support"))
  )
  var selected by remember { mutableStateOf("Pro") }
  Scaffold(topBar = { TopAppBar(title = { Text("Upgrade Plan") }) }) { padding ->
    LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
      item {
        Text("Choose your plan", style = MaterialTheme.typography.displayLarge.copy(fontSize = 26.sp, fontWeight = FontWeight.Bold))
        Text("Unlock full analytics power", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
      items(plans.size) { i ->
        val plan = plans[i]
        val isSelected = selected == plan.name
        Card(
          modifier = Modifier.fillMaxWidth(),
          border = if (plan.highlighted || isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
          colors = CardDefaults.cardColors(containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface)
        ) {
          Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
              Column {
                if (plan.highlighted) { Surface(color = MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.small) { Text(" POPULAR ", style = MaterialTheme.typography.labelSmall, color = Color.White, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)) }; Spacer(Modifier.height(4.dp)) }
                Text(plan.name, style = MaterialTheme.typography.titleLarge)
                Row(verticalAlignment = Alignment.Bottom) { Text(plan.price, style = MaterialTheme.typography.displayLarge.copy(fontSize = 28.sp, fontWeight = FontWeight.Bold)); Text(" ${plan.period}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant) }
              }
              if (isSelected) Icon(Icons.Default.CheckCircle, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
            }
            plan.features.forEach { feature -> Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) { Text("✓", color = MaterialTheme.colorScheme.primary); Text(feature, style = MaterialTheme.typography.bodyLarge) } }
            if (plan.name != "Free") {
              Button(onClick = { selected = plan.name; onSubscribe(plan.name) }, modifier = Modifier.fillMaxWidth()) {
                Text(if (isSelected && plan.name != "Free") "Current Plan" else "Choose ${plan.name}")
              }
            }
          }
        }
      }
      item { Spacer(Modifier.height(8.dp)); Text("Cancel anytime. No hidden fees.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.fillMaxWidth()) }
    }
  }
}
