package com.devradar.trendradar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.devradar.trendradar.ui.navigation.AppNavHost
import com.devradar.trendradar.ui.navigation.bottomNavItems
import com.devradar.trendradar.ui.theme.TrendRadarTheme
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      TrendRadarTheme {
        val navController = rememberNavController()
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route
        Scaffold(
          bottomBar = {
            NavigationBar {
              bottomNavItems.forEach { screen ->
                NavigationBarItem(
                  icon={Icon(screen.icon,contentDescription=screen.label)},
                  label={Text(screen.label)},
                  selected=currentRoute==screen.route,
                  onClick={
                    navController.navigate(screen.route){
                      popUpTo(navController.graph.startDestinationId){saveState=true}
                      launchSingleTop=true
                      restoreState=true
                    }
                  }
                )
              }
            }
          }
        ) { AppNavHost(navController) }
      }
    }
  }
}
