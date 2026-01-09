package com.pmec.arena2k26.ui.organiserHomeScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pmec.arena2k26.core.models.Match
import com.pmec.arena2k26.core.models.Team
import com.pmec.arena2k26.core.models.TeamSelection
import com.pmec.arena2k26.core.ui.theme.Arena2k26Theme
import com.pmec.arena2k26.ui.organiserHomeScreen.viewmodel.CreateMatchViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMatchScreen(
    viewModel: CreateMatchViewModel = viewModel(),
    onMatchCreated: () -> Unit
) {
    val viewModelState by viewModel.uiState.collectAsState()

    // Local UI state holders
    var matchName by remember { mutableStateOf("") }
    var venue by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Upcoming") }
    var selectionStatusFilter by remember { mutableStateOf("All") }
    var selectedPreviousMatches by remember { mutableStateOf<List<Match>>(emptyList()) }
    var participatingTeams by remember { mutableStateOf<List<Team>>(emptyList()) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var statusExpanded by remember { mutableStateOf(false) }
    var selectionStatusExpanded by remember { mutableStateOf(false) }
    var previousMatchesExpanded by remember { mutableStateOf(false) }

    val filteredTeams by remember(selectionStatusFilter, selectedPreviousMatches, viewModelState.allTeams) {
        derivedStateOf {
            if (selectedPreviousMatches.isEmpty()) {
                emptyList()
            } else {
                val teamIdsToFind = selectedPreviousMatches
                    .flatMap { it.teamSelections }
                    .filter { selectionStatusFilter == "All" || it.status == selectionStatusFilter }
                    .map { it.teamId }
                    .toSet()

                viewModelState.allTeams.filter { it.id in teamIdsToFind }
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                        datePickerState.selectedDateMillis?.let { millis ->
                            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            date = sdf.format(Calendar.getInstance().apply { timeInMillis = millis }.time)
                        }
                    },
                    enabled = datePickerState.selectedDateMillis != null
                ) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Cancel") } }
        ) { DatePicker(state = datePickerState) }
    }

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState()
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text("Select Time") },
            text = { Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { TimePicker(state = timePickerState) } },
            confirmButton = {
                TextButton(onClick = { showTimePicker = false; time = "${timePickerState.hour}:${timePickerState.minute}" }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showTimePicker = false }) { Text("Cancel") } }
        )
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        if (viewModelState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Text("Create Esports Match", style = MaterialTheme.typography.headlineSmall) }

                item { TextField(value = matchName, onValueChange = { matchName = it }, label = { Text("Match Name") }, modifier = Modifier.fillMaxWidth()) }
                item { TextField(value = venue, onValueChange = { venue = it }, label = { Text("Venue") }, modifier = Modifier.fillMaxWidth()) }

                item {
                    Row(Modifier.fillMaxWidth()) {
                        TextField(value = date, onValueChange = {}, label = { Text("Date") }, readOnly = true, trailingIcon = { Icon(Icons.Default.DateRange, "Select Date", Modifier.clickable { showDatePicker = true }) }, modifier = Modifier.weight(1f))
                        Spacer(Modifier.width(8.dp))
                        TextField(value = time, onValueChange = {}, label = { Text("Time") }, readOnly = true, trailingIcon = { Icon(Icons.Default.DateRange, "Select Time", Modifier.clickable { showTimePicker = true }) }, modifier = Modifier.weight(1f))
                    }
                }

                item {
                    ExposedDropdownMenuBox(expanded = statusExpanded, onExpandedChange = { statusExpanded = it }) {
                        TextField(value = status, onValueChange = {}, readOnly = true, label = { Text("Status") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) }, modifier = Modifier.menuAnchor().fillMaxWidth())
                        ExposedDropdownMenu(expanded = statusExpanded, onDismissRequest = { statusExpanded = false }) {
                            listOf("Upcoming", "Completed").forEach { DropdownMenuItem(text = { Text(it) }, onClick = { status = it; statusExpanded = false }) }
                        }
                    }
                }

                item { Divider(Modifier.padding(vertical = 8.dp)) }
                item { Text("Participant Selection", style = MaterialTheme.typography.titleMedium) }

                item {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.weight(1f)) {
                            ExposedDropdownMenuBox(expanded = selectionStatusExpanded, onExpandedChange = { selectionStatusExpanded = it }) {
                                TextField(value = selectionStatusFilter, onValueChange = {}, readOnly = true, label = { Text("Filter Status") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = selectionStatusExpanded) }, modifier = Modifier.menuAnchor())
                                ExposedDropdownMenu(expanded = selectionStatusExpanded, onDismissRequest = { selectionStatusExpanded = false }) {
                                    listOf("All", "Selected", "Not Selected").forEach { DropdownMenuItem(text = { Text(it) }, onClick = { selectionStatusFilter = it; selectionStatusExpanded = false }) }
                                }
                            }
                        }
                        Spacer(Modifier.width(8.dp))
                        Box(modifier = Modifier.weight(1f)) {
                            ExposedDropdownMenuBox(expanded = previousMatchesExpanded, onExpandedChange = { previousMatchesExpanded = it }) {
                                TextField(value = if (selectedPreviousMatches.isEmpty()) "Select Matches" else "${selectedPreviousMatches.size} selected", onValueChange = {}, readOnly = true, label = { Text("Filter Matches") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = previousMatchesExpanded) }, modifier = Modifier.menuAnchor())
                                ExposedDropdownMenu(expanded = previousMatchesExpanded, onDismissRequest = { previousMatchesExpanded = false }) {
                                    viewModelState.allPreviousMatches.forEach { match ->
                                        val isSelected = selectedPreviousMatches.any { it.id == match.id }
                                        DropdownMenuItem(text = { Row(verticalAlignment = Alignment.CenterVertically) { Checkbox(checked = isSelected, onCheckedChange = null); Spacer(Modifier.width(8.dp)); Text(match.matchName) } },
                                            onClick = {
                                                val currentSelection = selectedPreviousMatches.toMutableList()
                                                if (isSelected) { currentSelection.removeAll { it.id == match.id } } else { currentSelection.add(match) }
                                                selectedPreviousMatches = currentSelection
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                if (filteredTeams.isNotEmpty()) {
                    item { Text("Select Teams for this Match", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(top = 8.dp)) }
                    items(filteredTeams) { team ->
                        Row(Modifier.fillMaxWidth().clickable { val currentParticipants = participatingTeams.toMutableList(); if (currentParticipants.any { it.id == team.id }) { currentParticipants.removeAll { it.id == team.id } } else { currentParticipants.add(team) }; participatingTeams = currentParticipants }.padding(vertical = 8.dp), verticalAlignment = Alignment.Top) {
                            Checkbox(checked = participatingTeams.any { it.id == team.id }, onCheckedChange = null)
                            Spacer(Modifier.width(16.dp))
                            Column {
                                Text(team.teamName, style = MaterialTheme.typography.titleMedium)
                                team.members.find { it.isLeader }?.let { Text("Leader: ${it.name}", style = MaterialTheme.typography.bodySmall) }
                                Text("Players: ${team.members.joinToString { it.name }}", style = MaterialTheme.typography.bodySmall, lineHeight = 16.sp)
                            }
                        }
                    }
                } else {
                    item { Text("Select previous matches to see available teams.", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(vertical = 16.dp)) }
                }

                item {
                    Button(
                        onClick = {
                            val newMatch = Match(
                                matchName = matchName,
                                venue = venue,
                                date = date,
                                time = time,
                                status = status,
                                teamSelections = participatingTeams.map { TeamSelection(teamId = it.id, status = "Selected") }
                            )
                            viewModel.createMatch(newMatch)
                            onMatchCreated()
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                    ) { Text("Create Match") }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateMatchScreenPreview() {
    Arena2k26Theme {
        CreateMatchScreen(onMatchCreated = {})
    }
}
