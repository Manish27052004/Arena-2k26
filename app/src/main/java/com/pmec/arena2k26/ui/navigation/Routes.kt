package com.pmec.arena2k26.ui.navigation

object Routes {
    const val LOGIN_SCREEN = "login_screen"
    const val REGISTER_SCREEN = "register_screen"
    const val ORG_HOME_SCREEN = "org_home_screen"
    const val CREATE_MATCH_SCREEN = "create_match_screen"
    const val USER_HOME_SCREEN = "user_home_screen/{userName}"
    const val MATCH_DETAILS_SCREEN = "match_details_screen/{matchId}"

    // Helpers to create dynamic routes
    fun userHome(userName: String) = "user_home_screen/$userName"
    fun matchDetails(matchId: String) = "match_details_screen/$matchId"

    const val ARG_USER_NAME = "userName"
    const val ARG_MATCH_ID = "matchId"
}
