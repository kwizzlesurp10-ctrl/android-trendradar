package com.devradar.trendradar.ui.splash
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
@Composable
fun SplashScreen(onFinished: () -> Unit) {
  val scale = remember { Animatable(0.6f) }
  val alpha = remember { Animatable(0f) }
  LaunchedEffect(Unit) {
    scale.animateTo(1f, animationSpec=spring(dampingRatio=Spring.DampingRatioMediumBouncy, stiffness=Spring.StiffnessLow))
    alpha.animateTo(1f, animationSpec=tween(400))
    delay(1200)
    onFinished()
  }
  Box(
    Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFF1A0533),Color(0xFF0D1B4B)))),
    contentAlignment=Alignment.Center
  ) {
    Column(horizontalAlignment=Alignment.CenterHorizontally, verticalArrangement=Arrangement.spacedBy(12.dp),
      modifier=Modifier.scale(scale.value)) {
      Text("📡", fontSize=72.sp)
      Text("TrendRadar",
        style=MaterialTheme.typography.displayLarge.copy(fontWeight=FontWeight.Bold),
        color=Color.White)
      Text("Your Android analytics co-pilot",
        style=MaterialTheme.typography.bodyLarge,
        color=Color.White.copy(alpha=0.7f))
    }
  }
}
