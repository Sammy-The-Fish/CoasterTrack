package com.example.coastertrack.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.coastertrack.data.model.room.LocalRollercoasterEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface RollercoasterDao {

    // updates a rollercoasters ranking field
    @Query("UPDATE rollercoasters SET ranking = :rank WHERE rideId = :id")
    suspend fun updateRank(id: Int, rank: Int)


    // gets all rollercoasters by the time they were ridden
    @Query("SELECT * FROM rollercoasters ORDER BY timeRidden ASC")
    fun getRollercoastersByDate(): Flow<List<LocalRollercoasterEntity>>

    // gets all rollercoaster by there ranking
    @Query("SELECT * FROM rollercoasters ORDER BY ranking ASC")
    fun getRollercoasterByRanking(): Flow<List<LocalRollercoasterEntity>>

    // adds rollercoaster, or updates if already exciting
    @Upsert
    suspend fun addRollercoaster(rollercoaster: LocalRollercoasterEntity)

    // gets total number of rollercoaster matching id, will be 0 or 1
    @Query("SELECT COUNT(*) FROM rollercoasters WHERE rideId = :id")
    suspend fun countRollercoastersWithId(id: Int): Int

    // finds total number of rollercoasters in table
    @Query("SELECT COUNT(*) FROM rollercoasters")
    fun countRollercoasters(): Flow<Int>

    // finds total number of unique park ids in table
    @Query("SELECT COUNT(DISTINCT parkID) FROM rollercoasters")
    fun countParks(): Flow<Int>

    // deletes all rollercoasters
    @Query("DELETE FROM rollercoasters")
    suspend fun deleteALlItems()

    // deletes all rollercoasters where ride id = id
    @Query("DELETE FROM rollercoasters WHERE rideId =:id")
    suspend fun deleteItemById(id: Int)

    // finds total number of rollercoasters in a park
    @Query("SELECT COUNT(*) FROM rollercoasters WHERE parkID = :id")
    suspend fun getCountOfRollercoastersAtPark(id: Int): Int

}


