package com.example.carcarapplication.ui.screens

import android.app.TimePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carcarapplication.api_helpers.RetrofitClient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import retrofit2.HttpException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateReservationScreen(
    onNavigateToHome: () -> Unit,
    carID: Long
) {
    var startDate by remember { mutableStateOf<Long?>(null) }
    var endDate by remember { mutableStateOf<Long?>(null) }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // ISO 8601 Date Formatter
    val isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    // Helper to format a timestamp into an ISO string
    fun formatToISO(timestamp: Long?): String {
        return timestamp?.let {
            val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault())
            localDateTime.format(isoFormatter)
        } ?: "Select"
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 80.dp)
    ) {
        Text(
            text = "Create Reservation",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Start Date and Time Picker
        Button(onClick = { showStartDatePicker = true }) {
            Text(text = "Start Date: ${formatToISO(startDate)}")
        }
        if (showStartDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showStartDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        showStartDatePicker = false
                        showStartTimePicker = true
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showStartDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                val state = rememberDatePickerState()
                DatePicker(state = state)
                startDate = state.selectedDateMillis
            }
        }
        if (showStartTimePicker) {
            val timePickerState = rememberTimePickerState()
            TimePicker(state = timePickerState)
            Button(onClick = {
                startDate = startDate?.let {
                    Calendar.getInstance().apply {
                        timeInMillis = it
                        set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                        set(Calendar.MINUTE, timePickerState.minute)
                    }.timeInMillis
                }
                showStartTimePicker = false
            }) {
                Text("Set Start Time")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // End Date and Time Picker
        Button(onClick = { showEndDatePicker = true }) {
            Text(text = "End Date: ${formatToISO(endDate)}")
        }
        if (showEndDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showEndDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        showEndDatePicker = false
                        showEndTimePicker = true
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEndDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                val state = rememberDatePickerState()
                DatePicker(state = state)
                endDate = state.selectedDateMillis
            }
        }
        if (showEndTimePicker) {
            val timePickerState = rememberTimePickerState()
            TimePicker(state = timePickerState)
            Button(onClick = {
                endDate = endDate?.let {
                    Calendar.getInstance().apply {
                        timeInMillis = it
                        set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                        set(Calendar.MINUTE, timePickerState.minute)
                    }.timeInMillis
                }
                showEndTimePicker = false
            }) {
                Text("Set End Time")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { onNavigateToHome() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text("Cancel")
            }

            Button(
                onClick = {
                    if (startDate != null && endDate != null) {
                        scope.launch {
                            try {
                                // Convert the selected times to LocalDateTime before sending the request
                                val startTime = formatToISO(startDate)
                                val endTime = formatToISO(endDate)

                                Log.d("Reservation Creation", "Start Time: $startTime, End Time: $endTime")

                                val reservation = RetrofitClient.apiService.createReservation(
                                    carID = carID,
                                    startTime = startTime,
                                    endTime = endTime
                                )
                                Toast.makeText(
                                    context,
                                    "Reservation Created",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onNavigateToHome()
                            } catch (e: HttpException) {
                                // Handle HTTP errors
                                val errorBody = e.response()?.errorBody()?.string() ?: "Unknown error"
                                Log.e("CreateReservation", "Error: $errorBody", e)

                                // Show the error message in a toast
                                Toast.makeText(
                                    context,
                                    "Error: $errorBody",
                                    Toast.LENGTH_LONG
                                ).show()
                            } catch (e: Exception) {
                                // Handle general exceptions
                                Log.e("CreateReservation", "Unexpected Error: ${e.localizedMessage}", e)
                                Toast.makeText(
                                    context,
                                    "Failed to create reservation: ${e.localizedMessage}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        // Handle case where date/time is not selected
                        Toast.makeText(context, "Please select both Start and End times", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                modifier = Modifier.padding(end = 16.dp),
                enabled = startDate != null && endDate != null
            ) {
                Text("Save")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateReservationPreview() {
    CreateReservationScreen(
        onNavigateToHome = {},
        carID = 1L
    )
}