package com.example.carcarapplication.ui.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

// ISO 8601 Date Formatter
val isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

// Helper to format a timestamp into an ISO string
fun formatToISO(timestamp: Long?): String {
    return timestamp?.let {
        val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault())
        localDateTime.format(isoFormatter)
    } ?: "Select"
}