package com.example.carcarapplication.data_classes

import com.squareup.moshi.JsonClass
import java.io.Serializable
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class MaintenanceReport(
    val maintenanceType: String,
    val cost: Double,
    val receipt: List<String>, // Use String (e.g., URLs or file paths)
    override val id: Long,
    override val author_user: User,
    override val date: LocalDateTime,
    override val description: String,
    // override val changeLog: List<String>,
    override val trip: Trip
) : Report(id, author_user, date, description, trip), Serializable
