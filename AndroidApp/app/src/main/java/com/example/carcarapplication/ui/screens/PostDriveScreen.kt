package com.example.carcarapplication.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.carcarapplication.ui.utils.DriveState
import com.example.carcarapplication.ui.utils.formatElapsedTime

@Composable
fun PostDriveScreen(
    onNavigateToHome: () -> Unit
) {
    var distance by remember { mutableStateOf("") }
    var fuelAverage by remember { mutableStateOf("5.0") } //Average fuel usage in europe per 100 km idk
    val startTime = DriveState.finalStartTime.longValue
    val endTime = System.currentTimeMillis()
    val elapsedTime = formatElapsedTime(endTime - startTime)

    // Normalize input to replace ',' with '.'
    val distanceValue = distance.replace(',', '.').toDoubleOrNull() ?: 0.0
    val fuelConsumption = fuelAverage.replace(',', '.').toDoubleOrNull() ?: 0.0

    // Calculate total fuel usage
    val totalFuelUsage = if (distanceValue > 0) {
        (distanceValue / 100) * fuelConsumption
    } else 0.0

    val totalEmissions = totalFuelUsage * 2.31

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Summary", style = MaterialTheme.typography.titleLarge)
        HorizontalDivider(thickness = 2.dp)
        Text("Trip Length:  $elapsedTime")
        OutlinedTextField(
            value = distance,
            onValueChange = { distance = it },
            label = { Text("Distance (km)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            )
        )

        OutlinedTextField(
            value = fuelAverage,
            onValueChange = { fuelAverage = it },
            label = { Text("Fuel (l/100km)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            )
        )

        Text("Fuel used: $totalFuelUsage l")
        Text("Emissions: $totalEmissions kg CO₂")

        Button(
            onClick = {
                DriveState.isDriving.value = false
                onNavigateToHome()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Drive")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrevPostScreen() {
    PostDriveScreen(
        onNavigateToHome = {},
    )
}