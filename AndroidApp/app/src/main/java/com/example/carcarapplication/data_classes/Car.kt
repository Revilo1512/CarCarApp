package com.example.carcarapplication.data_classes

import java.io.Serializable

data class Car(
    val carID : Int,
    val carName : String,
    val brand : String,
    val model : String,
    val availabilityStatus : Boolean,
    val reports : List<Report>,
    val trips : List<Trip>
) : Serializable
