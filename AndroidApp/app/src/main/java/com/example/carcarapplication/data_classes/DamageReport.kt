package com.example.carcarapplication.data_classes

import com.squareup.moshi.JsonClass
import java.io.Serializable
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class DamageReport(
    val damageDetails: String,
    val damagePhotos: List<String?>, // Use String (e.g., URLs or file paths) instead of Image
    override val reportID: Long?,
    override val author_user: User,
    override val date: LocalDateTime,
    override val description: String,
    // override val changeLog: List<String>,
    override val trip: Trip,
    override val car: Car
) : Report(reportID, author_user, date, description, trip, car), Serializable

