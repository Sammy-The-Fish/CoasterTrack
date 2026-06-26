package com.example.coastertrack.data.repository

import com.example.coastertrack.data.local.RollercoasterDao
import com.example.coastertrack.data.model.room.LocalRollercoasterEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// allows UI and domain layers to interact with the rollercoaster database
class RollercoasterDatabaseRepository @Inject constructor(
    private val rollercoasterDao: RollercoasterDao
) {

    suspend fun updateRanking(rank: Int, id: Int) = rollercoasterDao.updateRank(id, rank)

    fun getAllRollercoastersByDate(): Flow<List<LocalRollercoasterEntity>> =
        rollercoasterDao.getRollercoastersByDate()

    fun getAllRollercoastersByRanking(): Flow<List<LocalRollercoasterEntity>> =
        rollercoasterDao.getRollercoasterByRanking()

    suspend fun addRollercoaster(rollercoaster: LocalRollercoasterEntity) =
        rollercoasterDao.addRollercoaster(rollercoaster)

    suspend fun countAllRollercoasterWithId(id: Int): Int =
        rollercoasterDao.countRollercoastersWithId(id)

    fun countRollercoasters(): Flow<Int> = rollercoasterDao.countRollercoasters()

    fun countParks(): Flow<Int> = rollercoasterDao.countParks()

    suspend fun deleteAllItems() = rollercoasterDao.deleteALlItems()

    suspend fun deleteItemById(id: Int) = rollercoasterDao.deleteItemById(id)

    suspend fun getCountOfRollercoastersAtPark(id: Int): Int =
        rollercoasterDao.getCountOfRollercoastersAtPark(id)
}