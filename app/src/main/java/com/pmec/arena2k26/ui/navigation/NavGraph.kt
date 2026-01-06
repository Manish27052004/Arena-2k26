package com.pmec.arena2k26.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pmec.arena2k26.ui.organiserHomeScreen.CreateMatchScreen
import com.pmec.arena2k26.ui.organiserHomeScreen.OrgHomeScreen
import com.pmec.arena2k26.ui.organiserHomeScreen.viewmodel.CreateMatchViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val viewModel: CreateMatchViewModel = viewModel()

    NavHost(navController = navController, startDestination = Routes.ORG_HOME_SCREEN) {
        composable(Routes.ORG_HOME_SCREEN) {
            OrgHomeScreen(
                viewModel = viewModel,
                onNavigateToCreateMatch = { navController.navigate(Routes.CREATE_MATCH_SCREEN) }
            )
        }
        composable(Routes.CREATE_MATCH_SCREEN) {
            CreateMatchScreen(
                viewModel = viewModel,
                onMatchCreated = { navController.popBackStack() }
            )
        }
    }
}
