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
import com.pmec.arena2k26.user.ui.auth.RegisterScreen
import com.pmec.arena2k26.user.ui.userHomeScreen.UserHomeScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val viewModel: CreateMatchViewModel = viewModel()

    NavHost(navController = navController, startDestination = Routes.LOGIN_SCREEN) {
        composable(Routes.LOGIN_SCREEN) {
            LoginScreen(
                onNavigateToUserHome = { userName ->
                    navController.navigate(Routes.userHome(userName)) {
                        popUpTo(Routes.LOGIN_SCREEN) { inclusive = true }
                    }
                },
                onNavigateToOrgHome = {
                    navController.navigate(Routes.ORG_HOME_SCREEN) {
                        popUpTo(Routes.LOGIN_SCREEN) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.REGISTER_SCREEN) {
            RegisterScreen(onRegistrationComplete = {
                navController.popBackStack()
            })
        }
        composable(Routes.USER_HOME_SCREEN, arguments = listOf(navArgument(Routes.ARG_USER_NAME) { type = NavType.StringType })) {
            val userName = it.arguments?.getString(Routes.ARG_USER_NAME) ?: ""
            UserHomeScreen(
                userName = userName,
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER_SCREEN)
                }
            )
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
