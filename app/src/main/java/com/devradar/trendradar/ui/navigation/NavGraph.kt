package com.devradar.trendradar.ui.navigation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.devradar.trendradar.ui.dashboard.DashboardScreen
import com.devradar.trendradar.ui.myapps.MyAppsScreen
import com.devradar.trendradar.ui.trends.TrendsScreen
import com.devradar.trendradar.ui.insights.InsightsScreen
import com.devradar.trendradar.ui.competitor.CompetitorScreen
import com.devradar.trendradar.ui.alerts.AlertsScreen
import com.devradar.trendradar.ui.feed.FeedScreen
sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
  object Dashboard : Screen("dashboard","Dashboard",Icons.Default.Dashboard)
  object MyApps : Screen("myapps","My Apps",Icons.Default.Apps)
  object Trends : Screen("trends","Trends",Icons.Default.TrendingUp)
  object Insights : Screen("insights","AI",Icons.Default.AutoAwesome)
  object Competitor : Screen("competitor","Compete",Icons.Default.Radar)
  object Alerts : Screen("alerts","Alerts",Icons.Default.Notifications)
  object Feed : Screen("feed","Feed",Icons.Default.Feed)
}
val bottomNavItems = listOf(Screen.Dashboard,Screen.MyApps,Screen.Trends,Screen.Insights,Screen.Competitor,Screen.Alerts,Screen.Feed)
@Composable
fun AppNavHost(navController: NavHostController) {
  NavHost(navController,startDestination=Screen.Dashboard.route) {
    composable(Screen.Dashboard.route){DashboardScreen()}
    composable(Screen.MyApps.route){MyAppsScreen()}
    composable(Screen.Trends.route){TrendsScreen()}
    composable(Screen.Insights.route){InsightsScreen()}
    composable(Screen.Competitor.route){CompetitorScreen()}
    composable(Screen.Alerts.route){AlertsScreen()}
    composable(Screen.Feed.route){FeedScreen()}
  }
}
