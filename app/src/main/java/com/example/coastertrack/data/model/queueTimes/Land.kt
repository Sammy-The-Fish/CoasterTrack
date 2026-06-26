package com.example.coastertrack.data.model.queueTimes

import kotlinx.serialization.Serializable

// represents land data class from queue time API
@Serializable
data class Land(
    val id: Int = 0,
    val name: String = "",
    val rides: List<Ride> = listOf<Ride>()
)
