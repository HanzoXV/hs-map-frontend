package com.example.hs_map_frontend.data.repositories

import com.example.hs_map_frontend.data.db_interface.AuthApiService
import com.example.hs_map_frontend.data.db_models.RegisterRequest
import com.example.hs_map_frontend.data.db_models.RegisterResponse
import retrofit2.Response

class AuthRepository(private val apiService: AuthApiService) {

    suspend fun register(request: RegisterRequest): Result<RegisterResponse> {
        return try {
            val response = apiService.registerUser(request)

            android.util.Log.d("AuthRepository", "HTTP status: ${response.code()}")
            android.util.Log.d("AuthRepository", "Error body: ${response.errorBody()?.string()}")
            android.util.Log.d("AuthRepository", "Response body: ${response.body()}")

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "UNKNOWN ERROR."
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            android.util.Log.d("AuthRepository", "Exception: ${e.message}")
            Result.failure(e)
        }
    }
}