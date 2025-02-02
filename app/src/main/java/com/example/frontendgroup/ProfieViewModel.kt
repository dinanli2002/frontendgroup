package com.example.frontendgroup

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendgroup.retrofit.NurseInterface
import com.example.frontendgroup.retrofit.RemoteNurseUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfieViewModel: ViewModel() {
    private val _remoteMessageUiState = MutableStateFlow<RemoteNurseUiState>(RemoteNurseUiState.Loading)
    val remoteMessageUiState: StateFlow<RemoteNurseUiState> = _remoteMessageUiState
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request()
            Log.d("ProfileViewModel", "Request URL: ${request.url}")
            val response = chain.proceed(request)
            Log.d("ProfileViewModel", "Response Status Code: ${response.code}")
            response
        }.build()
    private val api: NurseInterface = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/")
        // Ensure your backend is running
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(NurseInterface::class.java)
    fun getNurseId(nurseId: String) {
        viewModelScope.launch {
            _remoteMessageUiState.value = RemoteNurseUiState.Loading
            try {
                val response = api.getNurseId(nurseId.toInt())
                if (response != null) {
                    Log.d("ProfileViewModel", "Nurse fetched successfully: ${response.username}")
                    _remoteMessageUiState.value = RemoteNurseUiState.Success(response)
                } else {
                    Log.e("ProfileViewModel", "Response was null")
                    _remoteMessageUiState.value = RemoteNurseUiState.Error
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error fetching nurse: ${e.message}", e)
                _remoteMessageUiState.value = RemoteNurseUiState.Error
            }
        }
    }
}