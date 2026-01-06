package com.pmec.arena2k26.ui.organiserHomeScreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmec.arena2k26.data.repository.MatchRepository
import com.pmec.arena2k26.models.Match
import com.pmec.arena2k26.models.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class CreateMatchViewModel : ViewModel() {

    private val repository = MatchRepository()

    private val _uiState = MutableStateFlow(CreateMatchUiState())
    val uiState: StateFlow<CreateMatchUiState> = _uiState.asStateFlow()

    init {
        // Start listening to the real-time flows from the repository
        viewModelScope.launch {
            // Combine the two flows, so that we get an update whenever either of them changes
            combine(repository.getTeamsFlow(), repository.getMatchesFlow()) { teams, matches ->
                CreateMatchUiState(allTeams = teams, allPreviousMatches = matches, isLoading = false)
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun createMatch(match: Match) {
        viewModelScope.launch {
            val success = repository.createMatch(match)
            // No need to manually refresh the list.
            // The snapshot listener will automatically update the uiState.
        }
    }
}

data class CreateMatchUiState(
    val allTeams: List<Team> = emptyList(),
    val allPreviousMatches: List<Match> = emptyList(),
    val isLoading: Boolean = true
)
