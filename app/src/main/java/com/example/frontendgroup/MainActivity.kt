@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.frontendgroup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    var currentScreen by remember { mutableStateOf("main") } // Estado de la pantalla actual
    when (currentScreen) {
        "main" -> MainScreen(onNavigateToForm = { currentScreen = "form" }, onNavigateToSearch = {currentScreen = "searchNurse"})

        "form" -> FormScreen(
            onNavigateToNurseInfo = { currentScreen = "nurseInfo" },  // Cambiar la pantalla a nurseInfo
            onNavigateBack = { currentScreen = "main" }
        )
        "nurseInfo" -> NurseInfoScreen(onNavigateBack = { currentScreen = "main" })
        "searchNurse" -> SearchNurseScreen(
            onNavigateToNurseInfo = { currentScreen = "nurseInfo" },  // Cambiar la pantalla a nurseInfo
            onNavigateBack = { currentScreen = "main" }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(onNavigateToForm: () -> Unit,onNavigateToSearch: () -> Unit) {
    var showList by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Main Screen") })
        }
    ) { paddingValues ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues).
                padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { showList = !showList },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (showList) "Hide List" else "Show List")
            }
            if (showList) {
                NurseList(onBackPressed = { showList = false })
            }
            Button(
                onClick = onNavigateToForm,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
            Button(
                onClick = onNavigateToSearch,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Search")
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(onNavigateToNurseInfo: () -> Unit, onNavigateBack: () -> Unit) {
    var text1 by remember { mutableStateOf("") }
    var text2 by remember { mutableStateOf("") }
    var snackbarHostState = remember { SnackbarHostState() }
    var snackbarMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Form Screen") },
                navigationIcon = {
                    Button(onClick = onNavigateBack) { Text("Back") }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = text1,
                onValueChange = { text1 = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = text2,
                onValueChange = { text2 = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    // Verificar las credenciales
                    if (text1 == "nurse1" && text2 == "nurse1") {
                        snackbarMessage = "Login successful"
                        //onNavigateToNurseInfo() // Cambiar a la pantalla nurseInfo
                    } else {
                        snackbarMessage = "Login incorrect"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Validate Login")
            }

            LaunchedEffect(snackbarMessage) {
                if (snackbarMessage.isNotEmpty()) {
                    snackbarHostState.showSnackbar(snackbarMessage)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NurseInfoScreen(onNavigateBack: () -> Unit) {
    var showNurseList by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nurse Info Screen") },  // Corregido el título
                navigationIcon = {
                    Button(onClick = onNavigateBack) { Text("Back") }
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(onClick = { showNurseList = true }) {
                Text("Nurse Info")
            }

            if (showNurseList) {
                NurseList(onBackPressed = { showNurseList = false })
            }
        }
    }
}


@Composable
fun SearchNurseScreen(onNavigateToNurseInfo: () -> Unit, onNavigateBack: () -> Unit) {
    var text1 by remember { mutableStateOf("") }
    var text2 by remember { mutableStateOf("") }
    var snackbarHostState = remember { SnackbarHostState() }
    var snackbarMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Nurse") },
                navigationIcon = {
                    Button(onClick = onNavigateBack) { Text("Back") }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = text1,
                onValueChange = { text1 = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = text2,
                onValueChange = { text2 = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    // Verificar las credenciales
                    if (text1 == "nurse1" && text2 == "nurse1") {
                        snackbarMessage = "Login successful"
                        //onNavigateToNurseInfo() // Cambiar a la pantalla nurseInfo
                    } else {
                        snackbarMessage = "Login incorrect"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Validate Login")
            }

            LaunchedEffect(snackbarMessage) {
                if (snackbarMessage.isNotEmpty()) {
                    snackbarHostState.showSnackbar(snackbarMessage)
                }
            }
        }
    }

}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApp()
}

