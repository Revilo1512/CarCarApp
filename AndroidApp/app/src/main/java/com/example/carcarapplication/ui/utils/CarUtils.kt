package com.example.carcarapplication.ui.utils

import com.example.carcarapplication.data_classes.Trip
import java.time.LocalDateTime

fun isReserved(){
    TODO()
}

fun isAvailable(currentTime: LocalDateTime, carTrips: List<Trip>?): Boolean{
    if (carTrips != null) {
        if (carTrips.isEmpty()){
            return true
        }
        val nextTrip = carTrips.first()
        val timeBetween = java.time.Duration.between(currentTime, nextTrip.startTime)
        val gracePeriod = 120 //Minutes
        return timeBetween.toHours() > gracePeriod
    }

    return true
}
