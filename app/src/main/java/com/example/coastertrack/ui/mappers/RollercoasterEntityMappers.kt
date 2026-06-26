package com.example.coastertrack.ui.mappers

import com.example.coastertrack.data.model.room.LocalRollercoasterEntity
import com.example.coastertrack.domain.model.RollercoasterEntity

// converts rollercoaster entity to form used in database
fun RollercoasterEntity.toLocalEntity(time: Long): LocalRollercoasterEntity {
    return LocalRollercoasterEntity(
        name = name,
        rideId = id,
        parkID = parkID,
        height = height,
        length = length,
        duration = duration,
        pictureUrl = picture[0].url,
        inversions = inversions,
        timeRidden = time,
        speed = speed,
        ranking = 10000
    )
}