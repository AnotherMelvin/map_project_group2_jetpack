package id.ac.umn.stevenindriano.map_project_group2

import androidx.activity.result.ActivityResultLauncher
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import id.ac.umn.stevenindriano.map_project_group2.ui.createedit.CreateEditScreen
import id.ac.umn.stevenindriano.map_project_group2.ui.home.HomeScreen
import id.ac.umn.stevenindriano.map_project_group2.ui.navigation.NavDrawerMenu
import id.ac.umn.stevenindriano.map_project_group2.ui.navigation.NavScreenMenu
import id.ac.umn.stevenindriano.map_project_group2.ui.setting.SettingScreen

@Composable
fun MobileNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    innerPaddingValues: PaddingValues,
    sortList: String,
    requestPermissionLauncher: ActivityResultLauncher<String>
) {
    NavHost(
        navController = navController,
        startDestination = NavDrawerMenu.Home.route,
        modifier
            .padding(innerPaddingValues)
            .animateContentSize()
    ) {
        composable(route = NavDrawerMenu.Home.route) {
            HomeScreen(onNavigate = {id ->
                navController.navigate(route = "${NavScreenMenu.CreateEdit.route}?id=$id")
            }, sortList)
        }
        composable(
            route = "${NavScreenMenu.CreateEdit.route}?id={id}",
            arguments = listOf(navArgument("id"){type = NavType.IntType})
        ) {
            val id = it.arguments?.getInt("id") ?: -1
            CreateEditScreen(id = id, requestPermissionLauncher = requestPermissionLauncher) {
                navController.navigateUp()
            }
        }
        composable(route = NavDrawerMenu.Setting.route) {
            SettingScreen()
        }
    }
}