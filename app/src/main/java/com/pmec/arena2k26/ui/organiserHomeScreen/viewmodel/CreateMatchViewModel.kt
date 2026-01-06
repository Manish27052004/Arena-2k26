package com.pmec.arena2k26.ui.organiserHomeScreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmec.arena2k26.data.repository.MatchRepository
import com.pmec.arena2k26.models.Match
import com.pmec.arena2k26.models.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreateMatchViewModel : ViewModel() {

    private val repository = MatchRepository()

    private val _uiState = MutableStateFlow(CreateMatchUiState())
    val uiState: StateFlow<CreateMatchUiState> = _uiState.asStateFlow()

    init {
        fetchInitialData()
    }

    private fun fetchInitialData() {
        viewModelScope.launch {
            val teams = repository.getTeams()
            val matches = repository.getMatches()
            _uiState.value = _uiState.value.copy(
                allTeams = teams,
                allPreviousMatches = matches,
                isLoading = false
            )
        }
    }

    fun createMatch(match: Match) {
        viewModelScope.launch {
            val success = repository.createMatch(match)
            // You can expose a state to the UI to show a success/error message
        }
    }
}

data class CreateMatchUiState(
    val allTeams: List<Team> = emptyList(),
    val allPreviousMatches: List<Match> = emptyList(),
    val isLoading: Boolean = true
)
