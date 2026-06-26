package com.example.coastertrack.data.model.rcdb

import kotlinx.serialization.Serializable

// represents rollercoaster data type from rcdb api
@Serializable
data class Rollercoaster(
    val name: String,
    val id: Int,
    val park: Park,
    val city: String? = null,
    val state: String? = null,
    val country: String? = null,
    val region: String? = null,
    val link: String? = null,
    val make: String? = null,
    val model: String? = null,
    val type: String? = null,
    val design: String? = null,
    val stats: Statistics? = null,
    val mainPicture: Picture? = null,
    val pictures: List<Picture>? = null
) {
    @Serializable
    data class Park(
        val name: String,
        val id: Int
    )
}
