package com.devradar.trendradar.ui.insights
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
@Composable
fun InsightsScreen(vm: InsightsViewModel=hiltViewModel()) {
  val state by vm.state.collectAsState()
  Scaffold(topBar={TopAppBar(title={Text("AI Insights")})}) { padding ->
    Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
      OutlinedTextField(value=state.prompt,onValueChange=vm::onPromptChange,label={Text("Ask Gemini about your app...")},modifier=Modifier.fillMaxWidth())
      Spacer(Modifier.height(8.dp))
      Button(onClick=vm::generate,enabled=!state.isLoading,modifier=Modifier.fillMaxWidth()){if(state.isLoading)CircularProgressIndicator(Modifier.size(18.dp))else Text("Generate Insight")}
      state.error?.let{Text(it,color=MaterialTheme.colorScheme.error,modifier=Modifier.padding(top=8.dp))}
      Spacer(Modifier.height(16.dp))
      LazyColumn(verticalArrangement=Arrangement.spacedBy(12.dp)) {
        items(state.insights){insight->Card(Modifier.fillMaxWidth()){Text(insight,Modifier.padding(16.dp),style=MaterialTheme.typography.bodyLarge)}}
        if(state.insights.isEmpty()&&!state.isLoading) item{Text("Ask Gemini anything about your app performance, competitors, or market trends.",color=MaterialTheme.colorScheme.onSurfaceVariant)}
      }
    }
  }
}
