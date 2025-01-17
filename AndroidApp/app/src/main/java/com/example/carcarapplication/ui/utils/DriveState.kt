package com.example.carcarapplication.ui.utils

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf

object DriveState {
    var isDriving = mutableStateOf(false)
    var startTime = mutableLongStateOf(System.currentTimeMillis())
    var finalStartTime = mutableLongStateOf(0)
}

@SuppressLint("DefaultLocale")
fun formatElapsedTime(durationMillis: Long): String {
    val duration = durationMillis / 1000
    val hours = duration / 3600
    val minutes = (duration % 3600) / 60
    val seconds = duration % 60
    return if (hours >= 1) {
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }
}