package com.example.hs_map_frontend.data.db_models

data class RegisterRequest(
    val displayName: String,
    val email: String,
    val password: String,
    val preferredLanguage: String
)
