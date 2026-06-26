package com.example.coastertrack.ui.mappers

import com.example.coastertrack.data.model.rcdb.Park
import com.example.coastertrack.data.model.room.LocalParkEntity


// converts a park to the form used in the park database
fun Park.toLocalEntity(time: Long, visited: Boolean): LocalParkEntity {
    return LocalParkEntity(
        name = name,
        picUrl = mainPicture!!.url,
        timeVisited = time,
        parkID = id,
        visited = visited
    )
}