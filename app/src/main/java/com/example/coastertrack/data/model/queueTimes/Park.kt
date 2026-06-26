package com.example.coastertrack.data.model.queueTimes

import kotlinx.serialization.Serializable

// represents ride datatype from queue times API
@Serializable
data class Park(
    val lands: List<Land> = listOf<Land>(),
    val rides: List<Ride> = listOf<Ride>()
)
