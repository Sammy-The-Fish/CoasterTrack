package com.example.coastertrack.data.model.room

import androidx.room.Embedded
import androidx.room.Relation

// data class which connect visits and parks
data class VisitWithPark (
    @Embedded val visit: LocalVisitEntity,
    @Relation(
        parentColumn = "parkID",
        entityColumn = "parkID"
    )
    val park: LocalParkEntity
)