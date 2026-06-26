package com.example.coastertrack.data.serialiser

import com.example.coastertrack.data.model.rcdb.Statistics
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

// unique serializer for statistics due to inconsistencies with the JSON
object StatisticsSerializer : KSerializer<Statistics> {
    // defines elements in the json
    override val descriptor = buildClassSerialDescriptor(
        serialName = Statistics::class.simpleName!!
    ) {
        element<String?>("length")
        element<String?>("height")
        element<String?>("drop")
        element<String?>("speed")
        element<String?>("inversions")
        element<String?>("duration")
        element<String?>("capacity")
        element<String?>("arrangement")
    }

    override fun deserialize(decoder: Decoder): Statistics {
        // crash if input not json
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw IllegalStateException("Expected Json input")

        val jsonObject = jsonDecoder.decodeJsonElement().jsonObject


        val length: String? = getPossibleListObject(jsonObject["length"]) {
            it.jsonPrimitive.content.toDouble()
        }
        val height: String? = getPossibleListObject(jsonObject["height"]) {
            it.jsonPrimitive.content.toDouble()
        }
        val drop: String? = getPossibleListObject(jsonObject["drop"]) {
            it.jsonPrimitive.content.toDouble()
        }
        val speed: String? = getPossibleListObject(jsonObject["speed"]) {
            it.jsonPrimitive.content.toDouble()
        }
        val inversions: String? = getPossibleListObject(jsonObject["inversions"]) {
            it.jsonPrimitive.content.toInt()
        }
        val duration: String? = getPossibleListObject(jsonObject["duration"]) {
            it.jsonPrimitive.content.parseMMSS()
        }
        val capacity: String? = getPossibleListObject(jsonObject["capacity"]) {
            it.jsonPrimitive.content.toInt()
        }
        val arrangement: String? = jsonObject["arrangement"]?.jsonPrimitive?.content



        return Statistics(
            length = length,
            height = height,
            drop = drop,
            speed = speed,
            inversions = inversions,
            duration = duration,
            capacity = capacity,
            arrangement = arrangement
        )

    }


    /**
     * elements may be a list, therefore this function checks if they are, and
     * if so converts them to a string
     */
    private inline fun <R : Comparable<R>> getPossibleListObject(
        element: JsonElement?,
        sort: (JsonElement) -> R
    ): String? {
        return when (element) {
            is JsonArray -> {
                element.jsonArray.maxByOrNull { sort(it) }?.jsonPrimitive?.content
            }

            is JsonPrimitive -> {
                element.jsonPrimitive.content
            }

            else -> null
        }
    }

    override fun serialize(encoder: Encoder, value: Statistics) {
        TODO("No need to implement this function")
    }
}

/**
 * function pareses string in MM:SS format and converts it to seconds
 */
fun String.parseMMSS(): Int {
    val parts = this.split(":")
    val minutes = parts[0].toInt()
    val seconds = parts[1].toInt()
    return minutes * 60 + seconds
}