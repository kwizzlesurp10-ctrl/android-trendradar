package com.devradar.trendradar.ui.components
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@Composable
fun StatCard(modifier: Modifier=Modifier, label: String, value: String, delta: String) {
  Card(modifier) {
    Column(Modifier.padding(12.dp)) {
      Text(label,style=MaterialTheme.typography.labelSmall,color=MaterialTheme.colorScheme.onSurfaceVariant)
      Spacer(Modifier.height(4.dp))
      Text(value,style=MaterialTheme.typography.titleLarge)
      Text(delta,style=MaterialTheme.typography.labelSmall,color=if(delta.startsWith("+")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
    }
  }
}
