package com.match.matchmate.presentation.navigation


sealed class Screen(val route: String){
    data object HomeRoute : Screen("HomeScreen")
}