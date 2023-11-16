package id.ac.umn.stevenindriano.map_project_group2.ui.navigation

sealed class NavScreenMenu(val route: String) {
    object Create: NavScreenMenu("create_screen")
    object Edit: NavScreenMenu("edit_screen")
}
