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
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.frontendgroup.stricturedata.Login
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


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
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "form") {
        composable("form") {
            val viewModel: FormViewModel = viewModel()
            FormScreen(
                viewModel = viewModel,
                onNavigateToNurseInfo = { navController.navigate("nurseList") }
            )
        }
        composable("nurseList") {
            MainScreen(navController = navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Nurse List") })
        }
    ) { paddingValues ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues).
                padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            NurseList(onBackPressed = { navController.popBackStack() })
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(viewModel: FormViewModel, onNavigateToNurseInfo: () -> Unit) {
    val uiState = viewModel.uiState.collectAsState()
    var showRegistrationForm by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (showRegistrationForm) "Register" else "Login") }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = viewModel.snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (showRegistrationForm) {
                // Registration Form
                TextField(
                    value = uiState.value.username,
                    onValueChange = viewModel::onUsernameChange,
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = uiState.value.password,
                    onValueChange = viewModel::onPasswordChange,
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = onNavigateToNurseInfo,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Go to Nurse List")
                }
                TextButton(
                    onClick = { showRegistrationForm = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back to Login")
                }
            } else {
                // Login Form
                TextField(
                    value = uiState.value.username,
                    onValueChange = viewModel::onUsernameChange,
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = uiState.value.password,
                    onValueChange = viewModel::onPasswordChange,
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        viewModel.validateLogin(
                            onSuccess = onNavigateToNurseInfo
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Login")
                }
                TextButton(
                    onClick = { showRegistrationForm = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Registry")
                }
            }
        }
    }
}

class FormViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(Login())
    val uiState: StateFlow<Login> = _uiState
    val snackbarHostState = SnackbarHostState()
    fun onUsernameChange(newUsername: String) {
        _uiState.update { it.copy(username = newUsername) }
    }
    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }
    fun validateLogin(onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (_uiState.value.username == "nurse1" && _uiState.value.password == "nurse1") {
                snackbarHostState.showSnackbar("Login successful")
                onSuccess()
            } else {
                snackbarHostState.showSnackbar("Login incorrect")
            }
        }
    }
}


/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchNurseScreen(onNavigateToNurseInfo: () -> Unit, onNavigateBack: () -> Unit) {
    var text1 by remember { mutableStateOf("") }
    var snackbarHostState = remember { SnackbarHostState() }
    var snackbarMessage by remember { mutableStateOf("") }
    var results by remember { mutableStateOf(listOf<Nurse>()) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nurse Search Screen") },
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
            TextField(
                value = text1,
                onValueChange = { text1 = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                        snackbarMessage = "Search Successful"
                    results = searchNursesByName(text1)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Search")
            }
            LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 16.dp)) {
                items(results) { nurse ->
                    NurseItem(nurse = nurse)
                }
            }
        }
    }
}

fun searchNursesByName(query: String): List<Nurse> {
    return nurses.filter { it.name.contains(query, ignoreCase = true) }.take(3)
}*/



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApp()
}