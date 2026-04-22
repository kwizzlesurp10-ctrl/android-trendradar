package com.devradar.trendradar.ui.settings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
@Composable
fun SettingsScreen(vm: SettingsViewModel = hiltViewModel()) {
  val state by vm.state.collectAsState()
  var showKey by remember { mutableStateOf(false) }
  Scaffold(topBar = { TopAppBar(title = { Text("Settings") }) }) { padding ->
    LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
      item { Text("Appearance", style = MaterialTheme.typography.titleLarge) }
      item {
        Card(Modifier.fillMaxWidth()) {
          Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Dark Mode")
            Switch(checked = state.darkMode, onCheckedChange = { vm.toggleDarkMode() })
          }
        }
      }
      item { Spacer(Modifier.height(8.dp)); Text("Notifications", style = MaterialTheme.typography.titleLarge) }
      item {
        Card(Modifier.fillMaxWidth()) {
          Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            listOf(
              "All Notifications" to state.notificationsEnabled,
              "Rating Alerts" to state.ratingAlertsEnabled,
              "Crash Alerts" to state.crashAlertsEnabled,
              "Weekly Report" to state.weeklyReportEnabled
            ).forEachIndexed { i, (label, checked) ->
              Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(label, style = MaterialTheme.typography.bodyLarge)
                Switch(checked = checked, onCheckedChange = {
                  when(i) { 0 -> vm.toggleNotifications(); 1 -> vm.toggleRatingAlerts(); 2 -> vm.toggleCrashAlerts(); 3 -> vm.toggleWeeklyReport() }
                })
              }
            }
          }
        }
      }
      item { Spacer(Modifier.height(8.dp)); Text("API Keys", style = MaterialTheme.typography.titleLarge) }
      item {
        Card(Modifier.fillMaxWidth()) {
          Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
              value = state.geminiApiKey, onValueChange = vm::onGeminiKeyChange,
              label = { Text("Gemini API Key") }, modifier = Modifier.fillMaxWidth(),
              visualTransformation = if (showKey) VisualTransformation.None else PasswordVisualTransformation(),
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
              trailingIcon = {
                IconButton(onClick = { showKey = !showKey }) {
                  Icon(if (showKey) Icons.Default.VisibilityOff else Icons.Default.Visibility, null)
                }
              }
            )
            Button(onClick = vm::save, modifier = Modifier.fillMaxWidth()) {
              Text(if (state.isSaved) "Saved!" else "Save Keys")
            }
          }
        }
      }
      item {
        Spacer(Modifier.height(8.dp))
        Text("About", style = MaterialTheme.typography.titleLarge)
        Card(Modifier.fillMaxWidth().padding(top = 8.dp)) {
          Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Android TrendRadar", style = MaterialTheme.typography.titleLarge)
            Text("Version 1.0.0", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Package: com.devradar.trendradar", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }
    }
  }
}
