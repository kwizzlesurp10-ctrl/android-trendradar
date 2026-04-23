package com.devradar.trendradar.ui.components
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
@Composable
fun shimmerBrush(): Brush {
  val transition = rememberInfiniteTransition(label="shimmer")
  val translateAnim by transition.animateFloat(
    initialValue=0f, targetValue=1000f,
    animationSpec=infiniteRepeatable(tween(1000, easing=LinearEasing), RepeatMode.Restart),
    label="shimmer"
  )
  return Brush.linearGradient(
    colors=listOf(Color(0xFF2A2A2A), Color(0xFF3D3D3D), Color(0xFF2A2A2A)),
    start=Offset(translateAnim - 200f, 0f),
    end=Offset(translateAnim, 0f)
  )
}
@Composable
fun ShimmerCard(modifier: Modifier = Modifier) {
  val brush = shimmerBrush()
  Card(modifier) {
    Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.spacedBy(8.dp)) {
      Box(Modifier.fillMaxWidth(0.6f).height(18.dp).clip(RoundedCornerShape(4.dp)).background(brush))
      Box(Modifier.fillMaxWidth().height(12.dp).clip(RoundedCornerShape(4.dp)).background(brush))
      Box(Modifier.fillMaxWidth(0.8f).height(12.dp).clip(RoundedCornerShape(4.dp)).background(brush))
    }
  }
}
@Composable
fun ShimmerList(count: Int = 5) {
  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    repeat(count) { ShimmerCard(Modifier.fillMaxWidth()) }
  }
}
