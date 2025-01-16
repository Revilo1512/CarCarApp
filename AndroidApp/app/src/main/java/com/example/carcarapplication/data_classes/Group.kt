package com.example.carcarapplication.data_classes

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Group(
    val groupID : Long,
    val users: List<User>, // Default to an empty list
    val cars: List<Car>?,
    val name : String,
    val admin : User,
    // val pendingRequests : List<User>
) : Serializable
