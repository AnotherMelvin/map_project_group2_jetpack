package id.ac.umn.stevenindriano.map_project_group2.ui.navigation

sealed class NavScreenMenu(val route: String) {
    object Landing: NavScreenMenu("landing_screen")
    object CreateEdit: NavScreenMenu("create_edit_screen")
}
