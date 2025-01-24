package com.example.frontendgroup.retrofit

import com.example.frontendgroup.stricturedata.Nurse

sealed interface RemoteNurseUiState {
    data class Success(
        val remoteMessage: Nurse) : RemoteNurseUiState
    object Loading : RemoteNurseUiState
    object Error : RemoteNurseUiState
    object Cargant : RemoteNurseUiState
}