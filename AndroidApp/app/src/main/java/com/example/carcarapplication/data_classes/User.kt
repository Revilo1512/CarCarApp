package com.example.carcarapplication.data_classes

import java.io.Serializable

data class User(
    val userID : Int,
    val username : String,
    val email : String,
    // val pendingInvites: List<Group>
) : Serializable
