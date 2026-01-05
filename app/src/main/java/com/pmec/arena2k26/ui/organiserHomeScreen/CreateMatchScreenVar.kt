package com.pmec.arena2k26.ui.organiserHomeScreen

data class CreateMatchScreenVar(
    val team1: String = "",
    val team2: String = "",
    val venue: String = "",
    val date: String = "",
    val time: String = "",
    val status: String = "",
    val team1Participants: List<String> = emptyList(),
    val team2Participants: List<String> = emptyList(),
)
