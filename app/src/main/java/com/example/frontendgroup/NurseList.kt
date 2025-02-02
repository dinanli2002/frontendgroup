package com.example.frontendgroup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.frontendgroup.retrofit.RemoteNurseUiState
import com.example.frontendgroup.stricturedata.Nurse

@Composable
fun NurseList(onBackPressed: () -> Unit, viewModel: NurseListViewModel) {
    val nurseState by viewModel.remoteMessageUiState.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = onBackPressed,
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Back", style = MaterialTheme.typography.bodyLarge)
        }

        ShowNurseState(nurseState)

        Button(
            onClick = { viewModel.getAllNurses() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Load Nurses", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun ShowNurseState(state: RemoteNurseUiState) {
    when (state) {
        is RemoteNurseUiState.Success -> {
            SingleNurseState(nurse = state.remoteMessage)
        }
        is RemoteNurseUiState.SuccessList -> {
            SuccessState(nurses = state.remoteMessage)
        }
        RemoteNurseUiState.Loading -> {
            LoadingState()
        }
        RemoteNurseUiState.Error -> {
            ErrorState()
        }
        RemoteNurseUiState.Cargant -> {
            CargantState()
        }
    }
}

@Composable
fun LoadingState() {
    Text("Loading...", modifier = Modifier.padding(16.dp))
}

@Composable
fun ErrorState() {
    Text("Error loading data", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
}

@Composable
fun CargantState() {
    Text("Cargant...", modifier = Modifier.padding(16.dp))
}

@Composable
fun SuccessState(nurses: List<Nurse>) {
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

@Composable
fun SingleNurseState(nurse: Nurse) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Name: ${nurse.username}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Password: ${nurse.password}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Composable
fun NurseItem(nurse: Nurse) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "Name: ${nurse.username}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Password: ${nurse.password}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}