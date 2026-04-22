package com.devradar.trendradar.ui.myapps
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
@Composable
fun MyAppsScreen(vm: MyAppsViewModel=hiltViewModel()) {
  val state by vm.state.collectAsState()
  Scaffold(topBar={TopAppBar(title={Text("My Apps Hub")})}) { padding ->
    Column(Modifier.fillMaxSize().padding(padding).padding(24.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(16.dp)) {
      if(!state.isSignedIn) { Spacer(Modifier.height(48.dp)); Text("Connect Play Console",style=MaterialTheme.typography.titleLarge); Button(onClick=vm::signIn){Text("Sign in with Google")} }
      else { Text("Connected to Play Console",style=MaterialTheme.typography.titleLarge); Button(onClick=vm::syncApps,enabled=!state.isLoading){if(state.isLoading)CircularProgressIndicator(Modifier.size(18.dp))else Text("Sync Apps")}; OutlinedButton(onClick=vm::signOut){Text("Sign Out")}; state.error?.let{Text(it,color=MaterialTheme.colorScheme.error)} }
    }
  }
}
