package com.example.hs_map_frontend.ui.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.hs_map_frontend.data.db_models.RegisterRequest
import com.example.hs_map_frontend.data.repositories.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _uiState = mutableStateOf<RegisterUiState>(RegisterUiState.Idle)
    val uiState: State<RegisterUiState> = _uiState

    fun handleRegister(request: RegisterRequest) {
        android.util.Log.d("AuthViewModel", "handleRegister called with email: ${request.email}")
        _uiState.value = RegisterUiState.Loading

        viewModelScope.launch {
            android.util.Log.d("AuthViewModel", "Coroutine started")

            val result = repository.register(request)

            android.util.Log.d("AuthViewModel", "Got result: $result")

            result.onSuccess { response ->
                android.util.Log.d("AuthViewModel", "Success: ${response.message}")
                _uiState.value = RegisterUiState.Success(response.message)
            }.onFailure { error ->
                android.util.Log.d("AuthViewModel", "Failure: ${error.localizedMessage}")
                android.util.Log.d("AuthViewModel", "Failure cause: ${error.cause}")
                _uiState.value = RegisterUiState.Error(error.localizedMessage ?: "Unknown error")
            }
        }
    }

    class Factory(private val repository: AuthRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthViewModel(repository) as T
        }
    }
}

sealed interface RegisterUiState {
    object Idle : RegisterUiState
    object Loading : RegisterUiState
    data class Success(val message: String) : RegisterUiState
    data class Error(val error: String) : RegisterUiState
}