package com.example.coastertrack.data.repository

import com.example.coastertrack.data.api.RetrofitInstance
import com.example.coastertrack.data.model.queueTimes.Company
import com.example.coastertrack.data.model.queueTimes.Park

/**
 * exposes the queue times API to the UI
 */
class QueueTimeRepository {
    /**
     * get queue times for provided ID
     * @param id causes crash if the id is invalid
     */
    suspend fun getParkById(id: Int): Park {
        return RetrofitInstance.queueTimeApi.getParkFromId(id)
    }

    /**
     * gets list of available parks
     */
    suspend fun getParkList(): List<Company> {
        return RetrofitInstance.queueTimeApi.getParkList()
    }
}