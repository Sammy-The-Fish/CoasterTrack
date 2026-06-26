package com.example.coastertrack.data.model.queueTimes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// represents ride datatype from queue times API
@Serializable
data class Ride(
    val id: Int,
    val name: String,
    @SerialName("is_open") val isOpen: Boolean,
    @SerialName("wait_time") val waitTime: Int,
    @SerialName("last_updated") val lastUpdated: String
)
