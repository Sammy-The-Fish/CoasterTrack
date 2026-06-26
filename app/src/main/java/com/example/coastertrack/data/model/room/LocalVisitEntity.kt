package com.example.coastertrack.data.model.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// represents visit table from local database
@Entity(
    tableName = "visits",
    foreignKeys = [ForeignKey(
        entity = LocalParkEntity::class,
        parentColumns = ["parkID"],
        childColumns = ["parkID"]
    )]
)
data class LocalVisitEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val startTime: Long,
    val endTime: Long,
    val parkID: Int,
    val description: String,
)