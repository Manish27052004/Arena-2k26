package com.pmec.arena2k26.core.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.Date

@Serializable
data class Team(
    @DocumentId val id: String = "",
    val teamName: String = "",
    val members: List<Member> = emptyList()
) {
    @ServerTimestamp
    @Transient
    var registeredAt: Date? = null
}

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
