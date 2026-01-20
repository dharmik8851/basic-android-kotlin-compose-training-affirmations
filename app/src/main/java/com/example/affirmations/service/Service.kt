package com.example.affirmations.service

import com.example.affirmations.data.AffirmationResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class Service {

    // Affirmations API (no auth needed)
    interface AffirmationsApiService {
        @GET("/")
        suspend fun getRandomAffirmation(): AffirmationResponse
    }

    // Create Retrofit instances
    object ApiClient {
        private const val AFFIRMATIONS_BASE_URL = "https://www.affirmations.dev/"

        val affirmationsService: AffirmationsApiService by lazy {
            Retrofit.Builder()
                .baseUrl(AFFIRMATIONS_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AffirmationsApiService::class.java)
        }
    }

}