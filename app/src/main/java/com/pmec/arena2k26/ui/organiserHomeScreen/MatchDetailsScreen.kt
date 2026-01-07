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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pmec.arena2k26.models.Match
import com.pmec.arena2k26.ui.organiserHomeScreen.viewmodel.CreateMatchViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchDetailsScreen(viewModel: CreateMatchViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val match = uiState.selectedMatch

    var isEditMode by remember { mutableStateOf(false) }

    var editableMatchName by remember { mutableStateOf("") }
    var editableVenue by remember { mutableStateOf("") }
    var editableDate by remember { mutableStateOf("") }
    var editableTime by remember { mutableStateOf("") }
    var editableStatus by remember { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var statusExpanded by remember { mutableStateOf(false) }

    // Helper function to reset the local editable state to match the original data
    fun resetEditableState() {
        if (match != null) {
            editableMatchName = match.matchName
            editableVenue = match.venue
            editableDate = match.date
            editableTime = match.time
            editableStatus = match.status
        }
    }

    // Automatically populates/resets the editable fields when the match data first loads
    LaunchedEffect(match) {
        resetEditableState()
    }

    // This state is true only if the user has made a change while in edit mode
    val hasChanges by remember {
        derivedStateOf {
            isEditMode && match != null && (
                    editableMatchName != match.matchName ||
                            editableVenue != match.venue ||
                            editableDate != match.date ||
                            editableTime != match.time ||
                            editableStatus != match.status
                    )
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
                            editableDate = sdf.format(Calendar.getInstance().apply { timeInMillis = millis }.time)
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
                TextButton(onClick = { showTimePicker = false; editableTime = "${timePickerState.hour}:${timePickerState.minute}" }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showTimePicker = false }) { Text("Cancel") } }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Match Details") },
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Editing Mode:", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(5.dp))
                        IconButton(
                            onClick = {
                                if (isEditMode) {
                                    // If turning OFF, it now acts as a 'Cancel' action
                                    resetEditableState()
                                }
                                isEditMode = !isEditMode
                            },
                            modifier = Modifier.size(50.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = if (isEditMode) "ON" else "OFF")
                                Spacer(modifier = Modifier.width(2.dp))
                                Icon(
                                    imageVector = if (isEditMode) Icons.Default.CheckCircle else Icons.Default.Edit,
                                    modifier = Modifier.size(30.dp),
                                    contentDescription = if (isEditMode) "Cancel Editing" else "Enter Edit Mode"
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            if (match == null) {
                CircularProgressIndicator()
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Free Fire Match",
                        style = typography.titleLarge,
                        color = colorScheme.onSurfaceVariant
                    )

                    if (isEditMode) {
                        TextField(value = editableMatchName, onValueChange = { editableMatchName = it }, label = { Text("Match Name") }, modifier = Modifier.fillMaxWidth())
                    } else {
                        Text(text = match.matchName, style = typography.headlineMedium, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))

                    if (isEditMode) {
                        TextField(value = editableVenue, onValueChange = { editableVenue = it }, label = { Text("Venue") }, modifier = Modifier.fillMaxWidth())
                        TextField(value = editableDate, onValueChange = {}, readOnly = true, label = { Text("Date") }, modifier = Modifier.fillMaxWidth(), trailingIcon = { Icon(Icons.Default.DateRange, "Select Date", Modifier.clickable { showDatePicker = true }) })
                        TextField(value = editableTime, onValueChange = {}, readOnly = true, label = { Text("Time") }, modifier = Modifier.fillMaxWidth(), trailingIcon = { Icon(Icons.Default.DateRange, "Select Time", Modifier.clickable { showTimePicker = true }) })
                        ExposedDropdownMenuBox(expanded = statusExpanded, onExpandedChange = { statusExpanded = it }) {
                            TextField(
                                value = editableStatus,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Status") },
                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) }
                            )
                            ExposedDropdownMenu(expanded = statusExpanded, onDismissRequest = { statusExpanded = false }) {
                                listOf("Upcoming", "Completed").forEach { item ->
                                    DropdownMenuItem(text = { Text(item) }, onClick = { editableStatus = item; statusExpanded = false })
                                }
                            }
                        }
                    } else {
                        Text("Venue: ${match.venue}", style = typography.bodyLarge)
                        Text("Date: ${match.date}", style = typography.bodyLarge)
                        Text("Time: ${match.time}", style = typography.bodyLarge)
                        Text("Status: ${match.status}", style = typography.bodyLarge)
                    }

                    // The smart 'Save Changes' button
                    if (hasChanges) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = {
                                val updatedMatch = match.copy(
                                    matchName = editableMatchName,
                                    venue = editableVenue,
                                    date = editableDate,
                                    time = editableTime,
                                    status = editableStatus
                                )
                                viewModel.updateMatch(updatedMatch)
                                isEditMode = false // Exit edit mode after saving
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Save Changes")
                        }
                    }
                }
            }
        }
    }
}
