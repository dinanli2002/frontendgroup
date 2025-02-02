package com.example.frontendgroup

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendgroup.retrofit.NurseInterface
import com.example.frontendgroup.retrofit.RemoteNurseUiState
import com.example.frontendgroup.stricturedata.Nurse
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class NurseListViewModel : ViewModel() {
    // Usamos 'MutableStateFlow' para manejar el estado
    private val _remoteMessageUiState = MutableStateFlow<RemoteNurseUiState>(RemoteNurseUiState.Loading)
    val remoteMessageUiState: StateFlow<RemoteNurseUiState> = _remoteMessageUiState

    // Crear el cliente de OkHttp para logging y manejo de solicitudes
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request()
            Log.d("NurseListViewModel", "Request URL: ${request.url}")
            val response = chain.proceed(request)
            val headers = response.headers
            for (name in headers.names()) {
                Log.d("NurseListViewModel", "HEADERS: $name: ${headers[name]}")
            }
            Log.d("NurseListViewModel", "Response Status Code: ${response.code}")
            response
        }
        .build()


    // Crear la instancia de Retrofit para interactuar con la API
    private val api: NurseInterface = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/")  // Reemplaza con la URL correcta de tu servidor
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(NurseInterface::class.java)

    // Función para obtener la lista de enfermeras desde la API
    fun getAllNurses() {
        viewModelScope.launch {
            // Establecemos el estado de carga
            _remoteMessageUiState.value = RemoteNurseUiState.Loading

            try {
                // Realizamos la llamada a la API
                val response = api.getAllNurses()

                if (response.isSuccessful) {
                    val nurses = response.body() ?: emptyList()
                    Log.d("NurseListViewModel", "Successfully fetched nurses list: ${nurses.size}")

                    // Aquí estamos pasando la lista de enfermeras al estado de éxito
                    _remoteMessageUiState.value = RemoteNurseUiState.SuccessList(nurses)  // Usamos SuccessList para manejar la lista
                } else {
                    Log.e("NurseListViewModel", "Failed with code: ${response.code()}")
                    _remoteMessageUiState.value = RemoteNurseUiState.Error  // En caso de error con la respuesta
                }
            } catch (e: Exception) {
                Log.e("NurseListViewModel", "Error: ${e.message}", e)
                _remoteMessageUiState.value = RemoteNurseUiState.Error // En caso de excepción
            }
        }
    }
}
