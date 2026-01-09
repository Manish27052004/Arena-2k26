package com.pmec.arena2k26.data.repository

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.ktx.Firebase
import com.pmec.arena2k26.core.models.Match
import com.pmec.arena2k26.core.models.Team
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class MatchRepository {

    private val db = Firebase.firestore

    fun getMatchesFlow(): Flow<List<Match>> {
        return db.collection("matches").snapshots()
            .map { snapshot ->
                snapshot.toObjects(Match::class.java)
            }
    }

    fun getTeamsFlow(): Flow<List<Team>> {
        return db.collection("teams").snapshots()
            .map { snapshot ->
                snapshot.toObjects(Team::class.java)
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

    // Function to update an existing match document
    suspend fun updateMatch(match: Match): Boolean {
        return try {
            // Use the match's ID to find the document and set its new data
            db.collection("matches").document(match.id).set(match).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
