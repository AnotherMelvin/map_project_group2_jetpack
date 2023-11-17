package id.ac.umn.stevenindriano.map_project_group2.ui.navigation

sealed class NavScreenMenu(val route: String) {
    object CreateEdit: NavScreenMenu("create_edit_screen")
}
