package com.example.carcarapplication.data_classes

import java.io.Serializable

data class Car(
    val carID : Int,
    val name : String,
    val email : String,
) : Serializable
