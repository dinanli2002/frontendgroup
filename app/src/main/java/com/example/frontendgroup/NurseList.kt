package com.example.frontendgroup

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.frontendgroup.stricturedata.Nurse

// Lista de enfermeras with pictures
val nurses = listOf(
    Nurse("1", "nurse1", "nurse1"),
    Nurse("2","Dayanna", "dayannadw"),
    Nurse("3","Daniel", "danirios"),
    Nurse("4","Dinan", "dinanli"),
    Nurse("5","Alberto", "albertore"),
    Nurse("6","nurse2", "nurse2"),
    Nurse("7","nurse3", "nurse3"),
    Nurse("8","nurse4", "nurse4"),
    Nurse("9","nurse5", "nurse5"),
    Nurse("10","nurse6", "nurse6"),
    Nurse("11","nurse7", "nurse7"),
    Nurse("12","nurse8", "nurse8"),
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
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        /*Image(
            painter = painterResource(id = nurse. nursePicture),
            contentDescription = "Profile picture of ${nurse.name}",
            modifier = Modifier
                .size(64.dp)
                .padding(4.dp),
            contentScale = ContentScale.Crop
        )*/
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = "Name: ${nurse.username}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
            /*Text(
                text = "User: ${nurse.user}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )*/
            Text(
                text = "Password: ${nurse.password}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}