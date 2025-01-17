package com.example.carcarapplication.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.carcarapplication.ui.utils.DriveState
import com.example.carcarapplication.ui.utils.formatElapsedTime

@SuppressLint("DefaultLocale")
@Composable
fun DriveInfoScreen(
    onNavigateToPostDrive: () -> Unit
) {
    val elapsedTime = remember { mutableStateOf("00:00") }

    // Timer logic
    LaunchedEffect(DriveState.startTime.longValue) {
        while (DriveState.isDriving.value) {
            val currentTime = System.currentTimeMillis()
            val duration = currentTime - DriveState.startTime.longValue // seconds
            elapsedTime.value = formatElapsedTime(duration)
            kotlinx.coroutines.delay(1000L) // Update every second
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Trip Information",
            style = MaterialTheme.typography.titleLarge
        )
        Text("You've been driving for")
        Text(
            elapsedTime.value,
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            "Remember to take a 15 minute break every two hours of driving!",
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                DriveState.isDriving.value = false
                DriveState.finalStartTime.longValue = DriveState.startTime.longValue
                onNavigateToPostDrive()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Finish Drive")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDriveInfoScreen() {
    DriveState.isDriving.value = true
    DriveState.startTime.longValue =
        System.currentTimeMillis() - 3600000L // Simulate 1 hour elapsed
    DriveInfoScreen(onNavigateToPostDrive = {})
}