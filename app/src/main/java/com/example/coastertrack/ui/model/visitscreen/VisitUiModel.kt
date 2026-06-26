package com.example.coastertrack.ui.model.visitscreen

import java.util.Date

data class VisitUiModel(
    val startTime: Date,
    val endTime: Date,
    val id: Int,
    val parkName: String,
    val description: String,
    val parkPicUrl: String
)
