package com.devradar.trendradar.feature.splash
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.devradar.trendradar.feature.main.MainActivity
class SplashActivity : ComponentActivity() {
    override fun onCreate(s: Bundle?) {
        installSplashScreen().also { super.onCreate(s) }
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
