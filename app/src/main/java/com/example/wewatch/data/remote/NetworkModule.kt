package com.example.wewatch.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    // ⚠️ ВСТАВЬ СВОЙ API KEY!
    private const val API_KEY = "6f8bdc09"
    private const val BASE_URL = "https://www.omdbapi.com/"

    val api: OmdbApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OmdbApi::class.java)
    }

    fun getApiKey(): String = API_KEY
}