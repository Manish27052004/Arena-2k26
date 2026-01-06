package com.pmec.arena2k26.data.repository

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

import com.pmec.arena2k26.models.Match
import com.pmec.arena2k26.models.Team
import kotlinx.coroutines.tasks.await

class MatchRepository {

    private val db = Firebase.firestore

    suspend fun getMatches(): List<Match> {
        return try {
            db.collection("matches").get().await().toObjects(Match::class.java)
        } catch (e: Exception) {
            // In a real app, you'd want to handle this error gracefully
            emptyList()
        }
    }

    suspend fun getTeams(): List<Team> {
        return try {
            db.collection("teams").get().await().toObjects(Team::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun createMatch(match: Match): Boolean {
        return try {
            db.collection("matches").add(match).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
