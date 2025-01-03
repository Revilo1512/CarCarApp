package com.example.carcarapplication.data_classes

import java.io.Serializable

abstract class Report(
    open val reportID : Long,
    open val userID : Long,
    open val date : String,
    open val changeLog : List<String>,
    open val trip : Trip
) : Serializable