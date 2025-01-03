package com.example.carcarapplication.data_classes

import java.io.Serializable

data class Group(
    val groupID : Long,
    val name : String,
    val admin : User,
    val members : List<User>,
    val cars : List<Car>,
    val pendingRequests : List<User>
) : Serializable
