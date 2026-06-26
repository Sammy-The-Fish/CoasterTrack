package com.example.coastertrack.ui.model.parklookup


data class ParkUIModel(
    val name: String,
    val id: Int,
    var pic: PictureUIState = PictureUIState.Loading
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        return name.contains(query, ignoreCase = true)
    }
}
