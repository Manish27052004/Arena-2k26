package com.pmec.arena2k26.user.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pmec.arena2k26.core.models.Member
import com.pmec.arena2k26.core.ui.theme.Arena2k26Theme
import com.pmec.arena2k26.user.ui.auth.viewmodel.RegistrationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegistrationViewModel = viewModel(),
    onRegistrationComplete: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showMemberForm by remember { mutableStateOf(false) }

    // State for the member form itself
    var memberName by remember { mutableStateOf("") }
    var memberRegNo by remember { mutableStateOf("") }
    var memberBranch by remember { mutableStateOf("") }
    var memberSection by remember { mutableStateOf("") }
    var memberContact by remember { mutableStateOf("") }
    var memberEmail by remember { mutableStateOf("") }
    var isLeader by remember { mutableStateOf(false) }

    fun resetMemberForm() {
        memberName = ""; memberRegNo = ""; memberBranch = ""; memberSection = ""; memberContact = ""; memberEmail = ""; isLeader = false
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Team Registration") }) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text("Create Your Team", style = MaterialTheme.typography.headlineLarge)
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = uiState.teamName,
                    onValueChange = { viewModel.onTeamNameChange(it) },
                    label = { Text("Team Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider()
            }

            // Fixed lambda signature: itemsIndexed provides both index and item


            item {
                Spacer(modifier = Modifier.height(16.dp))
                AnimatedVisibility(visible = showMemberForm) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("Add New Member", style = MaterialTheme.typography.titleLarge)
                        TextField(value = memberName, onValueChange = { memberName = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
                        TextField(value = memberRegNo, onValueChange = { memberRegNo = it }, label = { Text("Registration Number") }, modifier = Modifier.fillMaxWidth())
                        Row(Modifier.fillMaxWidth()) {
                            TextField(value = memberBranch, onValueChange = { memberBranch = it }, label = { Text("Branch") }, modifier = Modifier.weight(1f))
                            Spacer(modifier = Modifier.width(8.dp))
                            TextField(value = memberSection, onValueChange = { memberSection = it }, label = { Text("Section") }, modifier = Modifier.weight(1f))
                        }
                        TextField(value = memberContact, onValueChange = { memberContact = it }, label = { Text("Contact Number") }, modifier = Modifier.fillMaxWidth())
                        TextField(value = memberEmail, onValueChange = { memberEmail = it }, label = { Text("Email ID") }, modifier = Modifier.fillMaxWidth())
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = isLeader, onCheckedChange = { isLeader = it })
                            Text("Set as Team Leader")
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            OutlinedButton(onClick = { showMemberForm = false; resetMemberForm() }) { Text("Cancel") }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = {
                                val newMember = Member(isLeader, memberName, memberRegNo, memberBranch, memberSection, memberContact, memberEmail)
                                viewModel.addMember(newMember)
                                showMemberForm = false
                                resetMemberForm()
                            }) { Text("Add Member") }
                        }
                        HorizontalDivider(modifier = Modifier.padding(top = 16.dp))
                    }
                }
                AnimatedVisibility(visible = !showMemberForm) {
                    OutlinedButton(onClick = { showMemberForm = true }, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add Team Member")
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {
                        viewModel.registerTeam()
                        onRegistrationComplete()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.isTeamNameValid && uiState.hasLeader
                ) {
                    Text("Register Team")
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    Arena2k26Theme {
        RegisterScreen(onRegistrationComplete = {})
    }
}
