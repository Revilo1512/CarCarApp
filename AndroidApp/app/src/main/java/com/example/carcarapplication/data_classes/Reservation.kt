package com.example.carcarapplication.data_classes

import com.squareup.moshi.JsonClass
import java.io.Serializable
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class Reservation(
    val reservationID: Long?,
    val user: User,
    val car: Car,
    val reservationStart: LocalDateTime,
    val reservationEnd: LocalDateTime
) : Serializable
