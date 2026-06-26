package com.example.coastertrack.ui.model.rollercoasterdetails

data class RollercoasterDetailsUiModel(
    val name: String,
    val id: Int,
    val parkId: Int,
    val height: Double?,
    val length: Double?,
    val inversions: Int?,
    val speed: Double?,
    val statistics: List<Statistic>,
    val pictures: List<Picture>,
) {


}
