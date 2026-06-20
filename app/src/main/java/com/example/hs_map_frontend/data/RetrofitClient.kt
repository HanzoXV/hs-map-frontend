package com.example.hs_map_frontend.data

import com.example.hs_map_frontend.data.db_interface.AuthApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue
import kotlin.jvm.java

object RetrofitClient {
    private const val BASE_URL = "http://10.138.50.125:5000/"

    val authService: AuthApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }
}