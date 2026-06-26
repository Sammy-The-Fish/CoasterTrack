package com.example.coastertrack.data.repository

import com.example.coastertrack.data.local.ParkDao
import com.example.coastertrack.data.model.room.LocalParkEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


// allows UI and domain layers to interact with the park database
class ParkDatabaseRepository @Inject constructor(
    private val parkDao: ParkDao
) {
    fun getAllParksByTimeVisited(): Flow<List<LocalParkEntity>> =
        parkDao.getAllVisitedParksByTimeVisited()


    suspend fun addPark(park: LocalParkEntity) = parkDao.addPark(park)

    suspend fun removePark(id: Int) = parkDao.removePark(id)

    suspend fun updateVisited(visited: Boolean, parkID: Int) =
        parkDao.updateVisited(visited, parkID)

    suspend fun deleteAllParks() = parkDao.deleteAllParks()

    suspend fun countParksWithIdWhereVisited(id: Int): Int =
        parkDao.countParksWithIdWhereVisited(id)

    fun countParks(): Flow<Int> = parkDao.countVisitedParks()

    suspend fun countParksWithId(id: Int): Int = parkDao.countParksWithId(id)
}