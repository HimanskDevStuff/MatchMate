package com.match.matchmate.presentation.navigation

/**
 * Defines the navigation route for the Matchmate screen.
 * Used by a type-safe navigation library.
 */

sealed class Screen(val route: String){
    data object HomeRoute : Screen("HomeScreen")
}