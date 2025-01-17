package com.example.carcarapplication.data_classes

import com.squareup.moshi.JsonClass
import java.io.Serializable
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class Trip(
    val tripID: Long,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val distance: Double,
    val fuelUsed: Double,
    val car: Car,
    val user: User
) : Serializable
