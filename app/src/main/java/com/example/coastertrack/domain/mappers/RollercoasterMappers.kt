package com.example.coastertrack.domain.mappers

import com.example.coastertrack.data.model.rcdb.Rollercoaster
import com.example.coastertrack.data.serialiser.parseMMSS
import com.example.coastertrack.domain.model.PictureEntity
import com.example.coastertrack.domain.model.RollercoasterEntity
import com.example.coastertrack.domain.model.StatisticEntity

/**
 * converts [Rollercoaster] object to [RollercoasterEntity]
 */
fun Rollercoaster.toEntity(): RollercoasterEntity {

    val tempStats = listOf<TemporaryStatistic?>(
        TemporaryStatistic(
            name = "Manufacturer",
            value = this.make
        ),
        TemporaryStatistic(
            name = "Model",
            value = this.model
        ),
        TemporaryStatistic(
            name = "Material",
            value = this.type
        ),
        TemporaryStatistic(
            name = "Design",
            value = this.design
        ),
        TemporaryStatistic(
            name = "Capacity",
            value = this.stats?.capacity
        ),
        TemporaryStatistic(
            name = "Arrangement",
            value = this.stats?.arrangement
        )
    )

    val stats = tempStats.mapNotNull { original ->
        original?.value?.let {
            StatisticEntity(original.name, it)
        }
    }

    val pics = if (this.pictures != null) {
        this.pictures.map {picture ->
            PictureEntity(
                id = picture.id,
                name = picture.name,
                copyDate = picture.copyDate,
                copyName = picture.copyName,
                url = picture.url
            )
        }
    } else listOf()



    val details = RollercoasterEntity(
        name = this .name,
        id = this.id,
        parkID = this.park.id,
        height = this.stats?.height?.toDouble(),
        length = this.stats?.length?.toDouble(),
        inversions = this.stats?.inversions?.toInt(),
        speed = this.stats?.speed?.toDouble(),
        statistics = stats,
        picture = pics,
        duration = this.stats?.duration?.parseMMSS()
    )

    return details


}
data class TemporaryStatistic (
    val name: String,
    val value: String?
)
