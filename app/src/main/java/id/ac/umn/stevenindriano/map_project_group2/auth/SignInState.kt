package id.ac.umn.stevenindriano.map_project_group2.auth

data class SignInState(
    val isSignInSucessful: Boolean = false,
    val signInError: String? = null
)
