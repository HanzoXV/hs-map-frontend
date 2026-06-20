package com.example.hs_map_frontend.data.db_interface

import com.example.hs_map_frontend.data.db_models.AuthResponse
import com.example.hs_map_frontend.data.db_models.LoginRequest
import com.example.hs_map_frontend.data.db_models.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/auth/register")
    suspend fun registerUser(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun loginUser(
        @Body request: LoginRequest
    ): Response<AuthResponse>
}
