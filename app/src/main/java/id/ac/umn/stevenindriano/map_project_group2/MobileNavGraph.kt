package id.ac.umn.stevenindriano.map_project_group2

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import id.ac.umn.stevenindriano.map_project_group2.ui.home.HomeScreen
import id.ac.umn.stevenindriano.map_project_group2.ui.navigation.NavDrawerMenu
import id.ac.umn.stevenindriano.map_project_group2.ui.setting.SettingScreen

@Composable
fun MobileNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    innerPaddingValues: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = NavDrawerMenu.Home.route,
        modifier.padding(innerPaddingValues).animateContentSize()
    ) {
        composable(NavDrawerMenu.Home.route) {
            HomeScreen()
        }
        composable(NavDrawerMenu.Setting.route) {
            SettingScreen()
        }
    }
}