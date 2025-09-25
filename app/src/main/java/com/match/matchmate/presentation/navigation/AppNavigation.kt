package com.match.matchmate.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.match.matchmate.presentation.matchMate.MatchMateRoot


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    Scaffold(
    ) { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            startDestination = Screen.HomeRoute.route
        ){
            composable(Screen.HomeRoute.route) {
                MatchMateRoot(
                    onEvent = {

                    }
                )
            }
        }
    }
}