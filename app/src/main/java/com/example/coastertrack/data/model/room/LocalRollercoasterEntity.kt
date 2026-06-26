package com.example.coastertrack.data.model.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


// represents rollercoaster table from local database
@Entity(
    tableName = "rollercoasters",
    foreignKeys = [ForeignKey(
        entity = LocalParkEntity::class,
        parentColumns = ["parkID"],
        childColumns = ["parkID"],
    )]
)
data class LocalRollercoasterEntity(
    val name: String,
    @PrimaryKey(autoGenerate = false)
    val rideId: Int,
    val parkID: Int,
    val height: Double?,
    val length: Double?,
    val inversions: Int?,
    val duration: Int?,
    val speed: Double?,
    val pictureUrl: String,
    val timeRidden: Long,
    val ranking: Int?
) {
}