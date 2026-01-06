package com.pmec.arena2k26.models

import com.google.firebase.firestore.DocumentId
import kotlinx.serialization.Serializable

@Serializable
data class Team(
    @DocumentId val id: String = "",
    val teamName: String = "",
    val registeredAt: String = "", // Using String for simplicity, can be converted to Date
    val members: List<Member> = emptyList()
)

@Serializable
data class Member(
    val isLeader: Boolean = false,
    val name: String = "",
    val registrationNumber: String = "",
    val branch: String = "",
    val section: String = "",
    val contactNumber: String = "",
    val email: String = ""
)