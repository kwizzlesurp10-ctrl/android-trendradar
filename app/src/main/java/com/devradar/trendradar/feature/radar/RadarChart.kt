package com.devradar.trendradar.feature.radar
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.devradar.trendradar.feature.radar.model.RadarMetric
import kotlin.math.*

data class RadarDataSet(val label: String, val color: Color, val metrics: List<RadarMetric>)

@Composable
fun SpiderRadarChart(datasets: List<RadarDataSet>, modifier: Modifier = Modifier, onAxisTapped: (String) -> Unit = {}) {
    if (datasets.isEmpty()) return
    val axisCount = datasets.first().metrics.size
    val labels = datasets.first().metrics.map { it.label }
    val prog by animateFloatAsState(1f, tween(1200, easing = FastOutSlowInEasing), label = "r")
    Canvas(modifier.pointerInput(Unit) {
        detectTapGestures { tap ->
            val c = Offset(size.width / 2f, size.height / 2f)
            val r = minOf(size.width, size.height) / 2f * 0.65f
            labels.forEachIndexed { i, l ->
                val a = (2 * PI / axisCount * i - PI / 2).toFloat()
                if ((tap - Offset(c.x + r * 1.3f * cos(a), c.y + r * 1.3f * sin(a))).getDistance() < 50f) onAxisTapped(l)
            }
        }
    }) {
        val c = Offset(size.width / 2f, size.height / 2f)
        val r = minOf(size.width, size.height) / 2f * 0.65f
        val gc = Color.Gray.copy(.3f)
        for (lv in 1..5) {
            val p = Path()
            for (i in 0..axisCount) { val a = (2 * PI / axisCount * (i % axisCount) - PI / 2).toFloat(); val pt = Offset(c.x + r * lv / 5f * cos(a), c.y + r * lv / 5f * sin(a)); if (i == 0) p.moveTo(pt.x, pt.y) else p.lineTo(pt.x, pt.y) }
            drawPath(p, gc, style = Stroke(1.dp.toPx()))
        }
        for (i in 0 until axisCount) { val a = (2 * PI / axisCount * i - PI / 2).toFloat(); drawLine(gc, c, Offset(c.x + r * cos(a), c.y + r * sin(a))) }
        datasets.forEach { ds ->
            val p = Path()
            ds.metrics.forEachIndexed { i, m ->
                val a = (2 * PI / axisCount * i - PI / 2).toFloat()
                val pt = Offset(c.x + r * m.value.coerceIn(0f,1f) * prog * cos(a), c.y + r * m.value.coerceIn(0f,1f) * prog * sin(a))
                if (i == 0) p.moveTo(pt.x, pt.y) else p.lineTo(pt.x, pt.y)
            }
            p.close()
            drawPath(p, ds.color.copy(.18f))
            drawPath(p, ds.color, style = Stroke(2.dp.toPx()))
        }
    }
}
