package com.example.coastertrack.data.api

import com.example.coastertrack.data.model.rcdb.Park
import com.example.coastertrack.data.model.rcdb.Rollercoaster
import retrofit2.http.GET
import retrofit2.http.Query

interface RcdbService {
    // request to get all rollercoasters in a parks information
    @GET("/rides")
    suspend fun getRollercoastersFromParkId(@Query("park.id") id: Int): List<Rollercoaster>

    // request to park information from park id
    @GET("/parks")
    suspend fun getParkByID(@Query("id") id: Int): List<Park>

    // get specific rollercoasters information
    @GET("/rides")
    suspend fun getRollercoasterById(@Query("id") id: Int): List<Rollercoaster>
}