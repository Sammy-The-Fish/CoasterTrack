package com.example.coastertrack.data.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

// represents parks table from local database
@Entity(tableName = "parks")
data class LocalParkEntity(
    val name: String,
    val picUrl: String,
    val timeVisited: Long,
    @PrimaryKey(autoGenerate = false)
    val parkID: Int,
    val visited: Boolean
)
