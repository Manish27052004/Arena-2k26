package com.pmec.arena2k26.ui.organiserHomeScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pmec.arena2k26.ui.organiserHomeScreen.viewmodel.CreateMatchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrgHomeScreen(
    viewModel: CreateMatchViewModel = viewModel(),
    onNavigateToCreateMatch: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var selectedWorkspace by remember { mutableStateOf("Manish") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Hey $selectedWorkspace") },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    Box {
                        Button(onClick = { expanded = true }) {
                            Text("Workspace")
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Manish") },
                                onClick = {
                                    selectedWorkspace = "Manish"
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("All") },
                                onClick = {
                                    selectedWorkspace = "All"
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    TextButton(onClick = { /*TODO Filter Logic*/ }) {
                        Text("All")
                    }
                    TextButton(onClick = { /*TODO Filter Logic*/ }) {
                        Text("Completed")
                    }
                    TextButton(onClick = { /*TODO Filter Logic*/ }) {
                        Text("Upcoming")
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigateToCreateMatch() },
                shape = RoundedCornerShape(16.dp),
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Match")
            }
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {
                items(uiState.allPreviousMatches) { match ->
                    MatchCard(match = match)
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun OrgHomeScreenPreview() {
    // This preview will show the loading state by default.
    // To preview with data, you would need a more complex setup with a fake ViewModel.
    OrgHomeScreen(onNavigateToCreateMatch = {})
}