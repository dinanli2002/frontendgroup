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

    private val _remoteMessageUiState = MutableStateFlow<RemoteNurseUiState>(RemoteNurseUiState.Loading)
    val remoteMessageUiState: StateFlow<RemoteNurseUiState> = _remoteMessageUiState

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


    private val api: NurseInterface = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(NurseInterface::class.java)

    fun getAllNurses() {
        viewModelScope.launch {

            _remoteMessageUiState.value = RemoteNurseUiState.Loading

            try {

                val response = api.getAllNurses()

                if (response.isSuccessful) {
                    val nurses = response.body() ?: emptyList()
                    Log.d("NurseListViewModel", "Successfully fetched nurses list: ${nurses.size}")


                    _remoteMessageUiState.value = RemoteNurseUiState.SuccessList(nurses)
                } else {
                    Log.e("NurseListViewModel", "Failed with code: ${response.code()}")
                    _remoteMessageUiState.value = RemoteNurseUiState.Error
                }
            } catch (e: Exception) {
                Log.e("NurseListViewModel", "Error: ${e.message}", e)
                _remoteMessageUiState.value = RemoteNurseUiState.Error
            }
        }
    }
}
