package com.example.coastertrack.domain.model


data class RollercoasterEntity(
    val name: String,
    val id: Int,
    val parkID: Int,
    val height: Double? = null,
    val length: Double?,
    val inversions: Int?,
    val speed: Double?,
    val duration: Int?,
    val picture: List<PictureEntity>,
    val statistics: List<StatisticEntity>
)