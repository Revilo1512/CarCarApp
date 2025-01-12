package com.example.carcarapplication.data_classes

import com.squareup.moshi.JsonClass
import java.io.Serializable
import java.time.LocalDateTime

@JsonClass(generateAdapter = false) // Abstract class should not generate its own adapter
abstract class Report(
    open val id: Long,
    open val author_user : User,
    open val date : LocalDateTime,
    open val description : String,
    // open val changeLog: List<String>,
    open val trip: Trip
) : Serializable