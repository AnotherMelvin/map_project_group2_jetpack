package id.ac.umn.stevenindriano.map_project_group2.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import id.ac.umn.stevenindriano.map_project_group2.R
import id.ac.umn.stevenindriano.map_project_group2.utils.Constants.Companion.HOME_ROUTE
import id.ac.umn.stevenindriano.map_project_group2.utils.Constants.Companion.SETTING_ROUTE

sealed class NavDrawerMenu(
    val route: String,
    @StringRes val label: Int,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val badgeCount: Int? = null
) {
    object Home: NavDrawerMenu(
        HOME_ROUTE,
        R.string.home,
        Icons.Outlined.Home,
        Icons.Filled.Home
    )
    object Setting: NavDrawerMenu(
        SETTING_ROUTE,
        R.string.settings,
        Icons.Outlined.Settings,
        Icons.Filled.Settings,
    )
}