package com.example.frontendgroup

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendgroup.retrofit.NurseInterface
import com.example.frontendgroup.retrofit.RemoteNurseUiState
import com.example.frontendgroup.stricturedata.Nurse
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginViewModel: ViewModel() {
    var remoteMessageUiState: RemoteNurseUiState by mutableStateOf(RemoteNurseUiState.Loading)
    private set
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request()
            Log.d("LoginViewModel", "Request URL: ${request.url}")
            val response = chain.proceed(request)
            val headers = response.headers
            for (name in headers.names()) {
                Log.d("LoginViewModel", " HEADERS: $name: ${headers[name]}")
            }
            Log.d("LoginViewModel", "Response Status Code: ${response.code}")
            response
        }.build()
    private val api: NurseInterface = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(NurseInterface::class.java)
    fun login(username: String, password: String) {
        viewModelScope.launch {
            remoteMessageUiState = RemoteNurseUiState.Loading
            try {
                val response = api.login(username, password)
                if (response.isSuccessful) {
                    val nurse = response.body()
                    if (nurse != null) {
                        Log.d("LoginViewModel", "Login successful: ${nurse.id}")
                        remoteMessageUiState = RemoteNurseUiState.Success(nurse)
                    } else {
                        Log.e("LoginViewModel", "Login response was empty")
                        remoteMessageUiState = RemoteNurseUiState.Error
                    }
                } else {
                    Log.e("LoginViewModel", "Login failed with code: ${response.code()}")
                    remoteMessageUiState = RemoteNurseUiState.Error
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Login error: ${e.message}", e)
                remoteMessageUiState = RemoteNurseUiState.Error
            }
        }
    }
    }