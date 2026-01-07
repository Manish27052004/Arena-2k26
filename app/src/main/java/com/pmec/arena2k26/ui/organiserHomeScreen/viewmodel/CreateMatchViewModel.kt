package com.pmec.arena2k26.ui.organiserHomeScreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmec.arena2k26.data.repository.MatchRepository
import com.pmec.arena2k26.models.Match
import com.pmec.arena2k26.models.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CreateMatchViewModel : ViewModel() {

    private val repository = MatchRepository()

    // Private flows for the raw data from the repository
    private val _teamsFlow = repository.getTeamsFlow()
    private val _matchesFlow = repository.getMatchesFlow()

    // A separate flow to hold the ID of the currently selected match
    private val _selectedMatchId = MutableStateFlow<String?>(null)

    // The main public UI state, derived from combining the data flows and the selected ID
    val uiState: StateFlow<CreateMatchUiState> = combine(
        _teamsFlow, _matchesFlow, _selectedMatchId
    ) { teams, matches, selectedId ->
        CreateMatchUiState(
            allTeams = teams,
            allPreviousMatches = matches,
            selectedMatch = matches.find { it.id == selectedId },
            isLoading = false // Becomes false once the flows emit their first values
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CreateMatchUiState(isLoading = true)
    )

    // This function now simply updates the ID, the UI state will reactively update
    fun getMatchById(matchId: String) {
        _selectedMatchId.value = matchId
    }

    fun createMatch(match: Match) {
        viewModelScope.launch {
            repository.createMatch(match)
        }
    }

    fun updateMatch(match: Match) {
        viewModelScope.launch {
            repository.updateMatch(match)
        }
    }
}

data class CreateMatchUiState(
    val allTeams: List<Team> = emptyList(),
    val allPreviousMatches: List<Match> = emptyList(),
    val selectedMatch: Match? = null,
    val isLoading: Boolean = true
)
