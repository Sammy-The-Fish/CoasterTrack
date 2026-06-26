package com.example.coastertrack.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.coastertrack.data.model.room.LocalVisitEntity
import com.example.coastertrack.data.model.room.VisitWithPark
import kotlinx.coroutines.flow.Flow

@Dao
interface VisitDao {

    // deletes all visits
    @Query("DELETE FROM visits")
    suspend fun deleteAllVisits()

    // gets all visits along with there respective parks ordered by start time
    @Transaction
    @Query("SELECT * FROM visits ORDER BY startTime ASC")
    fun getAllVisitsWithParksByStartTime(): Flow<List<VisitWithPark>>

    // adds or updates a visit
    @Upsert
    suspend fun addVisit(visit: LocalVisitEntity): Long

    // gets a specific visit with park by an Id
    @Transaction
    @Query("SELECT * FROM visits WHERE id = :id")
    suspend fun getVisitById(id: Int): VisitWithPark

    // deletes a visit where id match
    @Query("DELETE FROM visits WHERE id = :id")
    suspend fun deleteVisit(id: Int)

    // gets visits in range startTime and endTime
    @Query("SELECT * FROM visits WHERE startTime BETWEEN :startTime AND :endTime")
    fun getAllVisitsInTimeRange(
        startTime: Long,
        endTime: Long
    ): Flow<List<LocalVisitEntity>>

}