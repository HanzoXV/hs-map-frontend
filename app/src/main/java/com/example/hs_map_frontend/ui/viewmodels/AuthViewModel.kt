package com.example.hs_map_frontend.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.hs_map_frontend.data.db_models.LoginRequest
import com.example.hs_map_frontend.data.db_models.RegisterRequest
import com.example.hs_map_frontend.data.repositories.AuthRepository
import kotlinx.coroutines.launch

private const val TAG = "AuthViewModel"

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _loginState = mutableStateOf<LoginUiState>(LoginUiState.Idle)
    val loginState: State<LoginUiState> = _loginState

    private val _registerState = mutableStateOf<RegisterUiState>(RegisterUiState.Idle)
    val registerState: State<RegisterUiState> = _registerState

    fun handleLogin(request: LoginRequest) {
        Log.d(TAG, "handleLogin() → called with email=${request.email}")
        _loginState.value = LoginUiState.Loading
        viewModelScope.launch {
            repository.login(request)
                .onSuccess { response ->
                    Log.d(TAG, "handleLogin() → onSuccess: ${response.message}")
                    _loginState.value = LoginUiState.Success(response.message)
                }
                .onFailure { error ->
                    Log.e(TAG, "handleLogin() → onFailure: ${error.localizedMessage}")
                    _loginState.value = LoginUiState.Error(error.localizedMessage ?: "Unknown error")
                }
        }
    }

    fun handleRegister(request: RegisterRequest) {
        Log.d(TAG, "handleRegister() → called with email=${request.email}, displayName=${request.displayName}")
        _registerState.value = RegisterUiState.Loading
        viewModelScope.launch {
            repository.register(request)
                .onSuccess { response ->
                    Log.d(TAG, "handleRegister() → onSuccess: ${response.message}")
                    _registerState.value = RegisterUiState.Success(response.message)
                }
                .onFailure { error ->
                    Log.e(TAG, "handleRegister() → onFailure: ${error.localizedMessage}")
                    _registerState.value = RegisterUiState.Error(error.localizedMessage ?: "Unknown error")
                }
        }
    }

    fun resetLoginState() {
        Log.d(TAG, "resetLoginState()")
        _loginState.value = LoginUiState.Idle
    }

    fun resetRegisterState() {
        Log.d(TAG, "resetRegisterState()")
        _registerState.value = RegisterUiState.Idle
    }

    class Factory(private val repository: AuthRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthViewModel(repository) as T
        }
    }
}

sealed interface LoginUiState {
    object Idle : LoginUiState
    object Loading : LoginUiState
    data class Success(val message: String) : LoginUiState
    data class Error(val error: String) : LoginUiState
}

sealed interface RegisterUiState {
    object Idle : RegisterUiState
    object Loading : RegisterUiState
    data class Success(val message: String) : RegisterUiState
    data class Error(val error: String) : RegisterUiState
}