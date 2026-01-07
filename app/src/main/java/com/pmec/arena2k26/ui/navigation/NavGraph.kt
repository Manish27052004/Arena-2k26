package com.pmec.arena2k26.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pmec.arena2k26.ui.auth.LoginScreen
import com.pmec.arena2k26.ui.organiserHomeScreen.CreateMatchScreen
import com.pmec.arena2k26.ui.organiserHomeScreen.MatchDetailsScreen
import com.pmec.arena2k26.ui.organiserHomeScreen.OrgHomeScreen
import com.pmec.arena2k26.ui.organiserHomeScreen.viewmodel.CreateMatchViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val viewModel: CreateMatchViewModel = viewModel()

    NavHost(navController = navController, startDestination = Routes.LOGIN_SCREEN) {
        composable(Routes.LOGIN_SCREEN) {
            LoginScreen(onLoginClicked = {
                // For now, just navigate to the home screen
                navController.navigate(Routes.ORG_HOME_SCREEN) {
                    // Prevent going back to the login screen
                    popUpTo(Routes.LOGIN_SCREEN) { inclusive = true }
                }
            })
        }
        composable(Routes.ORG_HOME_SCREEN) {
            OrgHomeScreen(
                viewModel = viewModel,
                onNavigateToCreateMatch = { navController.navigate(Routes.CREATE_MATCH_SCREEN) },
                onMatchClick = { matchId ->
                    navController.navigate(Routes.matchDetails(matchId))
                }
            )
        }
        composable(Routes.CREATE_MATCH_SCREEN) {
            CreateMatchScreen(
                viewModel = viewModel,
                onMatchCreated = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.MATCH_DETAILS_SCREEN,
            arguments = listOf(navArgument(Routes.ARG_MATCH_ID) { type = NavType.StringType })
        ) {
            val matchId = it.arguments?.getString(Routes.ARG_MATCH_ID) ?: ""
            viewModel.getMatchById(matchId)
            MatchDetailsScreen(viewModel = viewModel)
        }
    }
}
