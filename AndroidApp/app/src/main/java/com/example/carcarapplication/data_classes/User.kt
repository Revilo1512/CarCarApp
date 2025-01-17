package com.example.carcarapplication.data_classes

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class User(
    val userID: Long,
    val name: String,
    val email: String,
    val password: String,
    //val groups : List<Group> = emptyList()
    //val pendingInvites: List<Group>
) : Serializable
