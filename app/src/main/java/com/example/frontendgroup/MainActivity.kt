@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.frontendgroup

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.frontendgroup.retrofit.RemoteNurseUiState
import com.example.frontendgroup.stricturedata.Login
import com.example.frontendgroup.stricturedata.Nurse
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
            val loginViewModel: LoginViewModel = viewModel()
            val formViewModel: FormViewModel = viewModel(factory = object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return FormViewModel(loginViewModel) as T
                }
            })
            val registerViewModel: RegisterViewModel = viewModel(factory = object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return RegisterViewModel() as T
                }
            })
            FormScreen(
                viewModel = formViewModel,
                registerViewModel = registerViewModel,
                onNavigateToNurseInfo = { nurseId ->
                    navController.navigate("nurseList/$nurseId")
                }
            )
        }
        composable("nurseList/{nurseId}") { backStackEntry ->
            val nurseId = backStackEntry.arguments?.getString("nurseId")?.toIntOrNull() ?: 1
            NurseListScreen(navController = navController, nurseId = nurseId)
        }
        composable("nurseProfile/{nurseId}") { backStackEntry ->
            val nurseId = backStackEntry.arguments?.getString("nurseId")?.toIntOrNull() ?: 1
            NurseProfileScreen(nurseId = nurseId)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NurseListScreen(navController: NavController,  nurseId: Int) {
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
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(nurses) { nurse ->
                    NurseItem(nurse = nurse, onClick = {
                        navController.navigate("nurseProfile/${nurse.id}")
                    })
                }
            }
            Button(
                onClick = { navController.navigate("nurseProfile/$nurseId") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Profile")
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(viewModel: FormViewModel, onNavigateToNurseInfo: (Int) -> Unit, registerViewModel: RegisterViewModel) {
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
                    onClick = {registerViewModel.register(uiState.value.username, uiState.value.password)},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Register")
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
                        viewModel.validateLogin {
                            nurseId -> onNavigateToNurseInfo(nurseId)
                    }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Login")
                }
                Button(
                    onClick = { showRegistrationForm = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Register")
                }
            }
        }
    }
}

class FormViewModel(private val loginViewModel: LoginViewModel) : ViewModel() {
    private val _uiState = MutableStateFlow(Login())
    val uiState: StateFlow<Login> = _uiState
    val snackbarHostState = SnackbarHostState()
    fun onUsernameChange(newUsername: String) {
        _uiState.update { it.copy(username = newUsername) }
    }
    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }
    fun validateLogin(onSuccess: (Int) -> Unit) {
        viewModelScope.launch {
            val username = _uiState.value.username
            val password = _uiState.value.password
            loginViewModel.login(username, password)
                when (val state = loginViewModel.remoteMessageUiState) {
                    is RemoteNurseUiState.Success -> {
                        snackbarHostState.showSnackbar("Login successful")
                        onSuccess(state.remoteMessage.id.toInt())
                    }
                    is RemoteNurseUiState.Error -> {
                        snackbarHostState.showSnackbar("Login failed")
                    }
                    else -> {
                        Unit
                    }
                }
        }
    }
}


@Composable
fun NurseItem(nurse: Nurse, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
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
}

@Composable
fun NurseProfileScreen(nurseId: Int) {
    val profileViewModel: ProfieViewModel = viewModel()
    val nurseState by profileViewModel.remoteMessageUiState.collectAsState()
    LaunchedEffect(nurseId) {
        profileViewModel.getNurseId(nurseId.toString())
    }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center) {
        when (nurseState) {
            is RemoteNurseUiState.Success -> {
                val nurse = (nurseState as RemoteNurseUiState.Success).remoteMessage
                Text("Nurse ID: ${nurse.id}")
                Text("Username: ${nurse.username}")
                Text("Password: ${nurse.password}")
            }
            is RemoteNurseUiState.Error -> {
                Text("Error loading nurse profile", color = Color.Red)
            }
            RemoteNurseUiState.Loading -> {
                Text("Loading...")
            }
            RemoteNurseUiState.Cargant -> {
                Text("Fetching data...")
            }
        }
    }
}



    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        MyApp()
    }

/*class FormViewModel(private val loginViewModel: LoginViewModel) : ViewModel() {
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
            /*val username = _uiState.value.username
            val password = _uiState.value.password
            loginViewModel.login(username, password)*/
            viewModelScope.launch {
                /*when (val state = loginViewModel.remoteMessageUiState) {
                    is RemoteNurseUiState.Success -> {
                        snackbarHostState.showSnackbar("Login successful")
                        onSuccess()
                    }
                    is RemoteNurseUiState.Error -> {
                        snackbarHostState.showSnackbar("Login failed")
                    }
                    else -> {
                        Unit
                    }*/
                    if (_uiState.value.username == "nurse1" && _uiState.value.password == "nurse1") {
                snackbarHostState.showSnackbar("Login successful")
                onSuccess()
            } else {
                snackbarHostState.showSnackbar("Login incorrect")
            }
                }
            }
        }
    }*/


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