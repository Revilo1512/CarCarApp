package com.example.carcarapplication.ui.screens

import android.util.Log
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.carcarapplication.api_helpers.RetrofitClient
import com.example.carcarapplication.data_classes.Trip
import com.example.carcarapplication.ui.utils.DriveState
import com.example.carcarapplication.ui.utils.formatElapsedTime
import com.example.carcarapplication.ui.utils.formatToISO
import kotlinx.coroutines.launch
import retrofit2.HttpException

@Composable
fun PostDriveScreen(
    onNavigateToHome: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var distance by remember { mutableStateOf("") }
    var fuelAverage by remember { mutableStateOf("5.0") } //Average fuel usage in europe per 100 km idk
    val startTime = DriveState.finalStartTime.longValue
    val endTime = System.currentTimeMillis()
    val formattedEndTime = formatToISO(endTime)
    val elapsedTime = formatElapsedTime(endTime - startTime)

    // Normalize input to replace ',' with '.'
    val distanceValue = distance.replace(',', '.').toDoubleOrNull() ?: 0.0
    val fuelUsed = fuelAverage.replace(',', '.').toDoubleOrNull() ?: 0.0

    // Calculate total fuel usage
    val totalFuelUsage = if (distanceValue > 0) {
        (distanceValue / 100) * fuelUsed
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
                scope.launch {
                    try {
                        RetrofitClient.apiService.modifyTrip(
                            tripID = DriveState.trip.value.tripID,
                            distance = distanceValue,
                            fuelUsed = fuelUsed,
                            endTime = formattedEndTime
                        )
                        DriveState.trip.value = Trip()
                    } catch (e: HttpException) {
                        // Handle HTTP errors
                        val errorBody = e.response()?.errorBody()?.string() ?: "Unknown error"
                        Log.e("ModifyTrip", "Error: $errorBody", e)
                    } catch (e: Exception) {
                        e.printStackTrace() // Handle error
                    }
                }
                onNavigateToHome()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = distance != null && fuelAverage != null
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