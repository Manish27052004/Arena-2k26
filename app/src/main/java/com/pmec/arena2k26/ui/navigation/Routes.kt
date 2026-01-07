package com.pmec.arena2k26.ui.navigation

object Routes {
    const val ORG_HOME_SCREEN = "org_home_screen"
    const val CREATE_MATCH_SCREEN = "create_match_screen"
    const val MATCH_DETAILS_SCREEN = "match_details_screen/{matchId}" // Dynamic route

    // Helper to create the route for a specific match
    fun matchDetails(matchId: String) = "match_details_screen/$matchId"

    const val ARG_MATCH_ID = "matchId" // Argument key
}
