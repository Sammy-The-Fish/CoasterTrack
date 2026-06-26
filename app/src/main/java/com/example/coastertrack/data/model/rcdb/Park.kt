package com.example.coastertrack.data.model.rcdb

import kotlinx.serialization.Serializable

// represents park data type from rcdb api
@Serializable
data class Park(
    val id: Int,
    val city: String? = null,
    val state: String? = null,
    val country: String? = null,
    val mainPicture: Picture? = null,
    val name: String
)
