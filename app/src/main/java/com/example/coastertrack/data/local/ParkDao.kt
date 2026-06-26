package com.example.coastertrack.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.coastertrack.data.model.room.LocalParkEntity
import kotlinx.coroutines.flow.Flow


// defines queries that can be executed on the database
@Dao
interface ParkDao {
    // get all parks which have been visited by the time they were visited
    @Query("SELECT * FROM parks WHERE visited = 1 ORDER BY timeVisited ASC")
    fun getAllVisitedParksByTimeVisited(): Flow<List<LocalParkEntity>>

    // add a park to the database, or update park if already on database
    @Upsert
    suspend fun addPark(park: LocalParkEntity)

    @Query("DELETE FROM parks WHERE parkID = :id")
    suspend fun removePark(id: Int)

    // update parkID park to visited parameter
    @Query("UPDATE parks SET visited = :visited WHERE parkID = :parkID")
    suspend fun updateVisited(visited: Boolean, parkID: Int)

    // delete all parks from table
    @Query("DELETE FROM parks")
    suspend fun deleteAllParks()

    // finds total number of parks which have been visited
    @Query("SELECT COUNT(*) FROM parks WHERE visited = 1")
    fun countVisitedParks(): Flow<Int>

    // finds total number of parks with id of id that have been visited will be 0 or 1
    @Query("SELECT COUNT(*) FROM parks WHERE parkID = :id AND visited = 1")
    suspend fun countParksWithIdWhereVisited(id: Int): Int

    // finds total number of parks which match id, will be 0 or 1
    @Query("SELECT COUNT(*) FROM parks WHERE parkID = :id")
    suspend fun countParksWithId(id: Int): Int



}




















