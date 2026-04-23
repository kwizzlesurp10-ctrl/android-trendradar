package com.devradar.trendradar.ui.widget
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.*
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.*
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.layout.*
import androidx.glance.text.*
import androidx.glance.unit.ColorProvider
import com.devradar.trendradar.MainActivity
class TrendRadarWidget : GlanceAppWidget() {
  @Composable
  override fun Content() {
    GlanceTheme {
      Column(
        modifier=GlanceModifier.fillMaxSize().background(Color(0xFF1E1E2E)).padding(16.dp).clickable(actionStartActivity<MainActivity>()),
        verticalAlignment=Alignment.CenterVertically
      ) {
        Text("TrendRadar", style=TextStyle(color=ColorProvider(Color.White), fontSize=16.sp, fontWeight=FontWeight.Bold))
        Spacer(GlanceModifier.height(8.dp))
        Row(GlanceModifier.fillMaxWidth()) {
          Column(GlanceModifier.defaultWeight()) {
            Text("Rating", style=TextStyle(color=ColorProvider(Color(0xFFAAAAAA)), fontSize=10.sp))
            Text("4.7 ★", style=TextStyle(color=ColorProvider(Color.White), fontSize=14.sp, fontWeight=FontWeight.Bold))
          }
          Column(GlanceModifier.defaultWeight()) {
            Text("Installs", style=TextStyle(color=ColorProvider(Color(0xFFAAAAAA)), fontSize=10.sp))
            Text("12.4K", style=TextStyle(color=ColorProvider(Color.White), fontSize=14.sp, fontWeight=FontWeight.Bold))
          }
          Column(GlanceModifier.defaultWeight()) {
            Text("Revenue", style=TextStyle(color=ColorProvider(Color(0xFFAAAAAA)), fontSize=10.sp))
            Text("\$1.2K", style=TextStyle(color=ColorProvider(Color.White), fontSize=14.sp, fontWeight=FontWeight.Bold))
          }
        }
        Spacer(GlanceModifier.height(8.dp))
        Text("Tap for full dashboard", style=TextStyle(color=ColorProvider(Color(0xFF6200EE)), fontSize=10.sp))
      }
    }
  }
}
class TrendRadarWidgetReceiver : GlanceAppWidgetReceiver() {
  override val glanceAppWidget = TrendRadarWidget()
}
