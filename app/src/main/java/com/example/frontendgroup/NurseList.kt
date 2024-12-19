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

data class Nurse(
    var name: String,
    var user: String,
    var password: String,
    var photoResId: Int,
    //modify the data class to accept url photo
   // var photoNurseUrl: String,
)

// Lista de enfermeras
val nurses = listOf(
    Nurse("nurse1", "nurse1", "nurse1",R.drawable.imagen2),
    Nurse("Dayanna", "dayannadw", "dayanna1",R.drawable.imagen),
    Nurse("Daniel", "danirios", "dani2",R.drawable.imagen2),
    Nurse("Dinan", "dinanli", "dinan3",R.drawable.imagen2),
    Nurse("Alberto", "albertore", "albert4",R.drawable.imagen2),
    Nurse("nurse2", "nurse2", "nurse2",R.drawable.imagen),
    Nurse("nurse3", "nurse3", "nurse3",R.drawable.imagen),
    Nurse("nurse4", "nurse4", "nurse4",R.drawable.imagen2),
    Nurse("nurse5", "nurse5", "nurse5",R.drawable.imagen),
    Nurse("nurse6", "nurse6", "nurse6",R.drawable.imagen),
    Nurse("nurse7", "nurse7", "nurse7",R.drawable.imagen2),
    Nurse("nurse8", "nurse8", "nurse8",R.drawable.imagen2),
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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
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
        // Imagen de perfil
        Image(
            painter = painterResource(id = nurse.photoResId),
            contentDescription = "Profile picture of ${nurse.name}",
            modifier = Modifier
                .size(64.dp)
                .padding(4.dp),
            contentScale = ContentScale.Crop // Ajustar imagen al contenedor
        )

        // Información de la enfermera
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
}