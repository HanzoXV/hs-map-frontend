package com.example.hs_map_frontend.data.db_models

data class LoginRequest(
    val email: String,
    val password: String,
)

data class AuthResponse(
    val message: String,
    val userId: String? = null,
    val email: String? = null,
    val displayName: String? = null,
    val preferredLanguage: String? = null

)