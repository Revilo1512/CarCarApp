package com.example.carcarapplication.data_classes

import java.io.Serializable
import java.time.LocalDateTime

data class Trip(
    val tripID : Long,
    val startTime : LocalDateTime,
    val endTime : LocalDateTime,
    val distance : Double,
    val fuelUsed : Double
) : Serializable
