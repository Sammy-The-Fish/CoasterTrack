package com.example.coastertrack.data.api

import com.example.coastertrack.data.model.queueTimes.Company
import com.example.coastertrack.data.model.queueTimes.Park
import retrofit2.http.GET
import retrofit2.http.Path

interface QueueTimesService {
    // request to obtain queue times from park id
    @GET("parks/{id}/queue_times.json")
    suspend fun getParkFromId(@Path("id") id: Int): Park

    // request to obtain list of parks
    @GET("parks.json")
    suspend fun getParkList(): List<Company>
}