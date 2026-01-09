package com.pmec.arena2k26.core.models

import com.google.firebase.firestore.DocumentId
import kotlinx.serialization.Serializable

@Serializable
data class Match(
    @DocumentId val id: String = "",
    val matchName: String = "",
    val venue: String = "",
    val date: String = "",
    val time: String = "",
    val status: String = "",
    val teamSelections: List<TeamSelection> = emptyList()
)

@Serializable
data class TeamSelection(
    val teamId: String = "",
    val status: String = "" // e.g., "Selected", "Not Selected"
)