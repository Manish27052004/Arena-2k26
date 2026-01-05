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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pmec.arena2k26.ui.theme.Arena2k26Theme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMatchScreen() {
    val screenWidth = (LocalConfiguration.current.screenWidthDp.dp)*0.4f

    var uiState by remember { mutableStateOf(CreateMatchScreenVar()) }
    val teams = listOf("CSE", "EE", "ME", "CE")
    val statuses = listOf("Upcoming", "Completed")

    var team1Expanded by remember { mutableStateOf(false) }
    var team2Expanded by remember { mutableStateOf(false) }
    var statusExpanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                        val selectedDate = Calendar.getInstance().apply {
                            timeInMillis = datePickerState.selectedDateMillis!!
                        }
                        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        uiState = uiState.copy(date = sdf.format(selectedDate.time))
                    },
                    enabled = datePickerState.selectedDateMillis != null
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState()
        DatePickerDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showTimePicker = false
                        uiState = uiState.copy(time = "${timePickerState.hour}:${timePickerState.minute}")
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Cancel") }
            }
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                TimePicker(state = timePickerState)
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ExposedDropdownMenuBox(expanded = team1Expanded, onExpandedChange = { team1Expanded = it }) {
                    TextField(
                        value = uiState.team1,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Team 1") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = team1Expanded) },
                        modifier = Modifier.height(60.dp).width(screenWidth).menuAnchor()
                    )
                    ExposedDropdownMenu(expanded = team1Expanded, onDismissRequest = { team1Expanded = false }) {
                        teams.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = { uiState = uiState.copy(team1 = it); team1Expanded = false }
                            )
                        }
                    }
                }
                Text(text = " vs ", modifier = Modifier.padding(horizontal = 8.dp))
                ExposedDropdownMenuBox(expanded = team2Expanded, onExpandedChange = { team2Expanded = it }) {
                    TextField(
                        value = uiState.team2,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Team 2") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = team2Expanded) },
                        modifier = Modifier.height(60.dp).width(screenWidth).menuAnchor()

                    )
                    ExposedDropdownMenu(expanded = team2Expanded, onDismissRequest = { team2Expanded = false }) {
                        teams.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = { uiState = uiState.copy(team2 = it); team2Expanded = false }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = uiState.venue,
                onValueChange = { uiState = uiState.copy(venue = it) },
                label = { Text("Venue") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = uiState.date,
                    onValueChange = { },
                    label = { Text("Date") },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = "Select Date",
                            modifier = Modifier.clickable { showDatePicker = true }
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                TextField(
                    value = uiState.time,
                    onValueChange = { },
                    label = { Text("Time") },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = "Select Time",
                            modifier = Modifier.clickable { showTimePicker = true }
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(expanded = statusExpanded, onExpandedChange = { statusExpanded = it }) {
                TextField(
                    value = uiState.status,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Status") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = statusExpanded, onDismissRequest = { statusExpanded = false }) {
                    statuses.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = { uiState = uiState.copy(status = it); statusExpanded = false }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Placeholder for participant selection
            Text("Participant selection UI will be implemented later.")

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = { /* TODO: Create match logic */ }) {
                Text("Create Match")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateMatchScreenPreview() {
    Arena2k26Theme {
        CreateMatchScreen()
    }
}
