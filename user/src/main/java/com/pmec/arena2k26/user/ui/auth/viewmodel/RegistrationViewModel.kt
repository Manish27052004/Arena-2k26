package com.pmec.arena2k26.user.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmec.arena2k26.core.models.Member
import com.pmec.arena2k26.core.models.Team

import com.pmec.arena2k26.user.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegistrationUiState(
    val teamName: String = "",
    val members: List<Member> = emptyList(),
    val isTeamNameValid: Boolean = false,
    val hasLeader: Boolean = false,
    val registrationStatus: String? = null // Success, Error, etc.
)

class RegistrationViewModel : ViewModel() {

    private val repository = UserRepository()
    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    fun onTeamNameChange(newName: String) {
        _uiState.update {
            it.copy(teamName = newName, isTeamNameValid = newName.isNotBlank())
        }
    }

    fun addMember(member: Member) {
        _uiState.update {
            val updatedMembers = it.members + member
            it.copy(
                members = updatedMembers,
                hasLeader = updatedMembers.any { m -> m.isLeader } // Check if a leader exists
            )
        }
    }

    fun registerTeam() {
        if (!_uiState.value.isTeamNameValid || !_uiState.value.hasLeader) return

        viewModelScope.launch {
            val newTeam = Team(
                teamName = _uiState.value.teamName,
                members = _uiState.value.members
            )
            val success = repository.createTeam(newTeam)
            _uiState.update {
                it.copy(registrationStatus = if (success) "Success" else "Error")
            }
        }
    }
}