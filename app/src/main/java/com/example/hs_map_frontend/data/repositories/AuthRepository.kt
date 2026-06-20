package com.example.hs_map_frontend.data.repositories

import android.util.Log
import com.example.hs_map_frontend.data.db_interface.AuthApiService
import com.example.hs_map_frontend.data.db_models.AuthResponse
import com.example.hs_map_frontend.data.db_models.LoginRequest
import com.example.hs_map_frontend.data.db_models.RegisterRequest

private const val TAG = "AuthRepository"

class AuthRepository(private val apiService: AuthApiService) {

    suspend fun register(request: RegisterRequest): Result<AuthResponse> {
        return try {
            Log.d(TAG, "register() → sending: email=${request.email}, displayName=${request.displayName}")
            val response = apiService.registerUser(request)
            Log.d(TAG, "register() → HTTP ${response.code()}")

            if (response.isSuccessful && response.body() != null) {
                Log.d(TAG, "register() → success: ${response.body()!!.message}")
                Result.success(response.body()!!)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "UNKNOWN ERROR."
                Log.e(TAG, "register() → server error ${response.code()}: $errorMsg")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "register() → exception: ${e::class.simpleName}: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun login(request: LoginRequest): Result<AuthResponse> {
        return try {
            Log.d(TAG, "login() → sending: email=${request.email}")
            val response = apiService.loginUser(request)
            Log.d(TAG, "login() → HTTP ${response.code()}")

            if (response.isSuccessful && response.body() != null) {
                Log.d(TAG, "login() → success: ${response.body()!!.message}")
                Result.success(response.body()!!)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "UNKNOWN ERROR."
                Log.e(TAG, "login() → server error ${response.code()}: $errorMsg")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "login() → exception: ${e::class.simpleName}: ${e.message}")
            Result.failure(e)
        }
    }
}