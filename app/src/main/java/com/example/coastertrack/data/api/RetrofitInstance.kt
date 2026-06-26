package com.example.coastertrack.data.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit


object RetrofitInstance {
    // create json object ignores unknown keys
    private val json = Json {
        ignoreUnknownKeys = true
    }

    //create retrofit object for queue-times.com
    private val queueTimesRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://queue-times.com")
            //automatically convert response to objects
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
            )
            .build()
    }

    // create retrofit object for rcdb-database.vercel.app
    private val rcdbRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://rcdb-database.vercel.app/")
            //automatically convert response to objects
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
            )
            .build()
    }


    // implement function defines in queue times interface
    val queueTimeApi: QueueTimesService by lazy {
        queueTimesRetrofit.create(QueueTimesService::class.java)
    }


    // implement function defines rcdb interface
    val rcdbApi: RcdbService by lazy {
        rcdbRetrofit.create(RcdbService::class.java)
    }
}