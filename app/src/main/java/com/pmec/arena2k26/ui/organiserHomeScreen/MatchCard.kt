package com.pmec.arena2k26.ui.organiserHomeScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pmec.arena2k26.models.Match
import com.pmec.arena2k26.ui.theme.Arena2k26Theme

@Composable
fun MatchCard(match: Match) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Text(
                    text = "Free Fire", // This can be dynamic later
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = match.matchName,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Venue: ${match.venue.ifEmpty { "Not specified" }}")
            Text("Date: ${match.date.ifEmpty { "Not specified" }}")
            Text("Time: ${match.time.ifEmpty { "Not specified" }}")
            Text("Status: ${match.status.ifEmpty { "Not specified" }}")
        }
    }
}

@Preview
@Composable
fun MatchCardPreview() {
    Arena2k26Theme {
        // Preview with a valid Match object constructor
        MatchCard(match = Match(matchName = "Qualifiers Day 1", venue = "Main Auditorium", date = "20/10/2024", time = "10:00", status = "Upcoming"))
    }
}
