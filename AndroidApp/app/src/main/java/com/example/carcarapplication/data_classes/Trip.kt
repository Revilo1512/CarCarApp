package com.example.carcarapplication.data_classes

import java.io.Serializable

data class Trip(
    val tripID : Long,
    val startTime : Double,
    val endTime : Double,
    val distance : Double,
    val fuelUsed : Double
) : Serializable
