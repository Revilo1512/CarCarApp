package com.example.carcarapplication.data_classes

import com.squareup.moshi.JsonClass
import java.io.Serializable
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class Trip(
    val tripID: Long = 0,
    val startTime: LocalDateTime = LocalDateTime.now(),
    val endTime: LocalDateTime? = null,
    val distance: Double? = null,
    val fuelUsed: Double? = null,
    val car: Car? = null,
    val user: User? = null
) : Serializable
