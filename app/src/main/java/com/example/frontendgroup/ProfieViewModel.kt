package com.example.frontendgroup

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendgroup.retrofit.NurseInterface
import com.example.frontendgroup.retrofit.RemoteNurseUiState
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfieViewModel: ViewModel() {
    var remoteMessageUiState: RemoteNurseUiState
            by mutableStateOf(RemoteNurseUiState.Cargant)
        private set
    fun getNurseId() {
        Log.d("exemple", "resposta")
        viewModelScope.launch {
            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request()
                    Log.d("exemple", "Request URL: ${request.url}")
                    val response = chain.proceed(request)
                    val headers = response.headers
                    for (name in headers.names()) {
                        Log.d("exemple", " HEADERS: $name: ${headers[name]}")
                    }
                    Log.d("Response", "Status Code: ${response.code}")
                    response // Retorna la resposta per continuar amb l'execució normal
                }.build()
            remoteMessageUiState = RemoteNurseUiState.Cargant
            try {
                val connexio = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/") // Ensure this URL is correct
                    .addConverterFactory(GsonConverterFactory.create()).client(client)
                    .build()
                val endPoint = connexio.create(NurseInterface::class.java)
                val resposta = endPoint.getNurseId("1")
                Log.d("exemple", "RESPOSTA ${resposta.password}")
                remoteMessageUiState = RemoteNurseUiState.Success(resposta)
            } catch (e: Exception) {
                Log.e("exemple", "RESPOSTA ERROR: ${e.message}", e)
                remoteMessageUiState = RemoteNurseUiState.Error
            }
        }
    }
}