package com.devradar.trendradar.ui.theme
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
private val DarkColors = darkColorScheme(primary=Primary,secondary=Secondary,background=Background,surface=Surface,onPrimary=OnPrimary,onBackground=OnBackground,onSurface=OnSurface)
private val LightColors = lightColorScheme(primary=Primary,secondary=Secondary)
@Composable
fun TrendRadarTheme(darkTheme: Boolean=isSystemInDarkTheme(), content: @Composable ()->Unit) {
  val colorScheme = if(Build.VERSION.SDK_INT>=31) { if(darkTheme) dynamicDarkColorScheme(LocalContext.current) else dynamicLightColorScheme(LocalContext.current) } else if(darkTheme) DarkColors else LightColors
  MaterialTheme(colorScheme=colorScheme,typography=Typography,content=content)
}
