package com.example.coastertrack.data.repository

import com.example.coastertrack.data.local.VisitDao
import com.example.coastertrack.data.model.room.LocalVisitEntity
import com.example.coastertrack.data.model.room.VisitWithPark
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// allows UI and domain layers to interact with the visit database
class VisitDatabaseRepository @Inject constructor(
    private val visitDao: VisitDao
) {


    suspend fun getVisitById(id: Int): VisitWithPark = visitDao.getVisitById(id)

    suspend fun deleteAllVisits() = visitDao.deleteAllVisits()

    fun getAllVisitsWithParksByStartTime() = visitDao.getAllVisitsWithParksByStartTime()

    suspend fun addVisit(visit: LocalVisitEntity): Long = visitDao.addVisit(visit)

    suspend fun deleteVisit(id: Int) = visitDao.deleteVisit(id)
    fun getAllVisitsInTimeRange(
        startTime: Long,
        endTime: Long
    ): Flow<List<LocalVisitEntity>> = visitDao.getAllVisitsInTimeRange(startTime, endTime)

}