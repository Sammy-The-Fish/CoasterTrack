package com.example.coastertrack.ui.model.rollercoasterdetails

data class RollercoasterDetailsUiModel(
    val name: String,
    val id: Int,
    val parkId: Int,
    val height: Statistic?,
    var length: Statistic?,
    val inversions: Statistic?,
    val speed: Statistic?,
    val statistics: List<Statistic>,
    val pictures: List<Picture>,
) {


}
