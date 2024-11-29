package com.example.frontendgroup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
    var currentScreen by remember { mutableStateOf("main") } // State for current screen
    when (currentScreen) {
        "main" -> MainScreen(onNavigateToForm = { currentScreen = "form" })
        "form" -> FormScreen(onNavigateBack = { currentScreen = "main" })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(onNavigateToForm: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Main Screen") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Button(onClick = onNavigateToForm) {
                Text("Login")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(onNavigateBack: () -> Unit) {
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
                    snackbarMessage = if (text1 == "nurse1" && text2 == "nurse1") {
                        "Login successful"
                    } else {
                        "Login incorrect"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
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