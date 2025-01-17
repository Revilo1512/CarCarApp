package com.example.carcarapplication.data_classes

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Car(
    val carID : Long,
    val carName : String,
    val brand : String,
    val model : String,
    val availabilityStatus : Boolean,
    // val reports : List<Report>?,
    //val reservations : List<Reservation>
    // val trips : List<Trip>?
) : Serializable
