package com.example.coastertrack.data.model.queueTimes

import kotlinx.serialization.Serializable

// represent company data class from queue times API
@Serializable
data class Company(
    val id: Int,
    val name: String,
    val parks: List<Park>
) {
    @Serializable
    data class Park (
        val id: Int,
        val name: String,
        val country: String,
        val continent: String,
        val longitude: String,
        val latitude: String,
        val timezone: String,
    )
}
