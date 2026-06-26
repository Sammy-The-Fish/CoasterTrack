package com.example.coastertrack.data.model.rcdb

import com.example.coastertrack.data.serialiser.StatisticsSerializer
import kotlinx.serialization.Serializable

// represents statistics from rcdb api
@Serializable(with = StatisticsSerializer::class)
data class Statistics (
    val length: String? = null,
    val height: String? = null,
    val drop: String? = null,
    val speed: String? = null,
    val inversions: String? = null,
    val duration: String? = null,
    val capacity: String? = null,
    val arrangement: String? = null,
)