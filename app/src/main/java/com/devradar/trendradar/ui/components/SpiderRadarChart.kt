package com.devradar.trendradar.ui.components
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import kotlin.math.cos
import kotlin.math.sin
@Composable
fun SpiderRadarChart(modifier: Modifier=Modifier, labels: List<String>, datasets: List<Pair<List<Float>,Color>>) {
  val animProgress by animateFloatAsState(targetValue=1f, animationSpec=tween(1200, easing=EaseOutCubic), label="radar")
  Canvas(modifier) {
    val cx=size.width/2f; val cy=size.height/2f
    val r=minOf(cx,cy)*0.72f
    val n=labels.size
    val rings=5
    for(ring in 1..rings) {
      val rr=r*ring/rings
      val p=Path()
      for(i in 0 until n) { val a=2*Math.PI/n*i-Math.PI/2; val x=(cx+rr*cos(a)).toFloat(); val y=(cy+rr*sin(a)).toFloat(); if(i==0) p.moveTo(x,y) else p.lineTo(x,y) }
      p.close(); drawPath(p,Color.Gray.copy(.25f)); drawPath(p,Color.Gray.copy(.4f),style=Stroke(1f))
    }
    for(i in 0 until n) { val a=2*Math.PI/n*i-Math.PI/2; drawLine(Color.Gray.copy(.4f),Offset(cx,cy),Offset((cx+r*cos(a)).toFloat(),(cy+r*sin(a)).toFloat())) }
    datasets.forEach { (vals,color) ->
      val p=Path()
      vals.forEachIndexed { i,v ->
        val a=2*Math.PI/n*i-Math.PI/2; val rv=r*v.coerceIn(0f,1f)*animProgress
        val x=(cx+rv*cos(a)).toFloat(); val y=(cy+rv*sin(a)).toFloat()
        if(i==0) p.moveTo(x,y) else p.lineTo(x,y)
      }
      p.close(); drawPath(p,color.copy(.18f)); drawPath(p,color,style=Stroke(2.dp.toPx()))
    }
    labels.forEachIndexed { i,label ->
      val a=2*Math.PI/n*i-Math.PI/2; val lx=(cx+(r+28f)*cos(a)).toFloat(); val ly=(cy+(r+28f)*sin(a)).toFloat()
      drawContext.canvas.nativeCanvas.drawText(label,lx,ly,android.graphics.Paint().apply{color=android.graphics.Color.WHITE;textSize=28f;textAlign=android.graphics.Paint.Align.CENTER})
    }
  }
}
