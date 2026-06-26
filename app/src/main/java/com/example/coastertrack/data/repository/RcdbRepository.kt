package com.example.coastertrack.data.repository

import com.example.coastertrack.data.api.RetrofitInstance
import com.example.coastertrack.data.model.rcdb.Park
import com.example.coastertrack.data.model.rcdb.Rollercoaster

/**
 * exposes the Rcdb API to the UI
 */
class RcdbRepository {
    /**
     * gets a list of rollercoaster in a park with the parks id
     * @return if [id] is invalid, function will return an empty list
     */
    suspend fun getRollercoastersFromParkId(id: Int): List<Rollercoaster> {
        return RetrofitInstance.rcdbApi.getRollercoastersFromParkId(id)
    }

    /**
     * gets a specific park based on it's [id]
     * @return list will always be one element long, unless [id] is invalid
     */
    suspend fun getParkByID(id: Int): List<Park> {
        return RetrofitInstance.rcdbApi.getParkByID(id)
    }


    /**
     * gets a specific rollercoaster based on it's [id]
     * @return list will be one element long, unless [id] is invalid
     */
    suspend fun getRollercoasterById(id: Int): List<Rollercoaster> {
        return RetrofitInstance.rcdbApi.getRollercoasterById(id)
    }
}