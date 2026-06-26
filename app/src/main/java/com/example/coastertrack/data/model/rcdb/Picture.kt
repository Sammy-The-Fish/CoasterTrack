package com.example.coastertrack.data.model.rcdb

import kotlinx.serialization.Serializable

// represents picture data type from the rcdb API
@Serializable
data class Picture(
    val id: Int,
    val name: String,
    val url: String,
    val copyName: String,
    val copyDate: String,
)