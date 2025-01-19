package com.example.carcarapplication.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.carcarapplication.api_helpers.RetrofitClient
import com.example.carcarapplication.data_classes.DamageReport
import com.example.carcarapplication.data_classes.MaintenanceReport
import com.example.carcarapplication.data_classes.Trip
import com.example.carcarapplication.ui.utils.DriveState
import com.example.carcarapplication.ui.utils.ReportState
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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Summary", style = MaterialTheme.typography.headlineMedium)
        HorizontalDivider(thickness = 2.dp)
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
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
                Spacer(modifier = Modifier.height(8.dp))
                Text("Fuel used: $totalFuelUsage l")
            }
        }

        //Text("Emissions: $totalEmissions kg CO₂")
        Card(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text("Select Report Type", style = MaterialTheme.typography.titleMedium)
                RadioButtonGroup(
                    options = listOf("No Report", "Maintenance", "Crash"),
                    selectedOption = ReportState.selectedReportType.value,
                    onOptionSelected = { ReportState.selectedReportType.value = it }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Conditional Form Display
                when (ReportState.selectedReportType.value) {
                    "Maintenance" -> MaintenanceReportForm()
                    "Crash" -> DamageReportForm()
                    else -> Text("No additional report needed.")
                }
            }
        }


        Spacer(modifier = Modifier.height(8.dp))

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
                        when (ReportState.selectedReportType.value) {
                            "Maintenance" -> {
                                RetrofitClient.apiService.createMaintenance(
                                    carID = DriveState.trip.value.car!!.carID,
                                    tripID = DriveState.trip.value.tripID,
                                    description = ReportState.maintenanceReport.value.description,
                                    type = ReportState.maintenanceReport.value.maintenanceType,
                                    cost = ReportState.maintenanceReport.value.cost
                                )
                            }

                            "Crash" -> {
                                RetrofitClient.apiService.createDamage(
                                    carID = DriveState.trip.value.car!!.carID,
                                    tripID = DriveState.trip.value.tripID,
                                    description = ReportState.damageReport.value.description,
                                    damageDetails = ReportState.damageReport.value.damageDetails
                                )
                            }
                        }

                        //Reset Values
                        DriveState.trip.value = Trip()
                        ReportState.selectedReportType.value = "No Report"
                        ReportState.damageReport.value = DamageReport()
                        ReportState.maintenanceReport.value = MaintenanceReport()
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
            enabled = distance.isNotEmpty() && fuelAverage.isNotEmpty()
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

@Composable
fun RadioButtonGroup(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column {
        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onOptionSelected(option) }
            ) {
                RadioButton(
                    selected = selectedOption == option,
                    onClick = { onOptionSelected(option) }
                )
                Text(option, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun MaintenanceReportForm() {
    var description by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("0.0") }
    var maintenanceType by remember { mutableStateOf("") }
    Column {
        Text("Maintenance Report")
        OutlinedTextField(
            value = description,
            onValueChange = {
                description = it
                ReportState.maintenanceReport.value = ReportState.maintenanceReport.value.copy(
                    description = description
                )
            },
            label = { Text("Title") }
        )
        OutlinedTextField(
            value = maintenanceType,
            onValueChange = {
                maintenanceType = it
                ReportState.maintenanceReport.value = ReportState.maintenanceReport.value.copy(
                    maintenanceType = maintenanceType
                )
            },
            label = { Text("Damage Details") }
        )
        OutlinedTextField(
            value = cost,
            onValueChange = {
                cost = it
                val costValue = cost.replace(',', '.').toDoubleOrNull() ?: 0.0
                ReportState.maintenanceReport.value = ReportState.maintenanceReport.value.copy(
                    cost = costValue
                )
            },
            label = { Text("Cost") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            )
        )
    }
}

@Composable
fun DamageReportForm() {
    var description by remember { mutableStateOf("") }
    var damageDetails by remember { mutableStateOf("") }
    Column {
        Text("Damage Report")
        OutlinedTextField(
            value = description,
            onValueChange = {
                description = it
                ReportState.damageReport.value = ReportState.damageReport.value.copy(
                    description = description
                )
            },
            label = { Text("Title") }
        )
        OutlinedTextField(
            value = damageDetails,
            onValueChange = {
                damageDetails = it
                ReportState.damageReport.value = ReportState.damageReport.value.copy(
                    damageDetails = damageDetails
                )
            },
            label = { Text("Damage Details") }
        )
    }
}