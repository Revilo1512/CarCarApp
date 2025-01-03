package com.example.carcarapplication.data_classes

import android.media.Image
import java.io.Serializable

data class MaintananceReport(
    val type : String,
    val cost : Double,
    val receipt : List<Image>,
    override val reportID: Long,
    override val userID: Long,
    override val date: String,
    override val changeLog: List<String>,
    override val trip: Trip
) : Serializable, Report(reportID, userID, date, changeLog, trip)
