package com.example.frontendgroup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Nurse(
    var name: String,
    var user: String,
    var password: String,
)

// Lista de enfermeras
val nurses = listOf(
    Nurse("nurse1", "nurse1", "nurse1"),
    Nurse("Dayanna", "dayannadw", "dayanna1"),
    Nurse("Daniel", "danirios", "dani2"),
    Nurse("Dinan", "dinanli", "dinan3"),
    Nurse("Alberto", "albertore", "albert4"),
    Nurse("nurse2", "nurse2", "nurse2"),
    Nurse("nurse3", "nurse3", "nurse3"),
    Nurse("nurse4", "nurse4", "nurse4"),
    Nurse("nurse5", "nurse5", "nurse5"),
    Nurse("nurse6", "nurse6", "nurse6"),
    Nurse("nurse7", "nurse7", "nurse7"),
    Nurse("nurse8", "nurse8", "nurse8"),
)

@Composable
fun NurseList(onBackPressed: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = onBackPressed,
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Back", style = MaterialTheme.typography.bodyLarge)
        }
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            items(nurses) { nurse ->
                NurseItem(nurse = nurse)
            }
        }
    }
}

@Composable
fun NurseItem(nurse: Nurse) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = "Name: ${nurse.name}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "User: ${nurse.user}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = "Password: ${nurse.password}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}
