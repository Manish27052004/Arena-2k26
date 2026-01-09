package com.pmec.arena2k26.user.data.repository

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pmec.arena2k26.core.models.Team
import kotlinx.coroutines.tasks.await

class UserRepository {

    private val db = Firebase.firestore

    suspend fun createTeam(team: Team): Boolean {
        return try {
            // The @ServerTimestamp annotation on the Team model handles the timestamp automatically.
            // We no longer need to set it manually.
            db.collection("teams").add(team).await()
            true
        } catch (e: Exception) {
            // In a real app, log this error
            false
        }
    }
}
