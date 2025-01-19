package com.example.carcarapplication.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carcarapplication.TestValues
import com.example.carcarapplication.api_helpers.RetrofitClient
import com.example.carcarapplication.data_classes.Car
import com.example.carcarapplication.data_classes.DamageReport
import com.example.carcarapplication.data_classes.MaintenanceReport
import com.example.carcarapplication.data_classes.Report
import com.example.carcarapplication.data_classes.Reservation
import com.example.carcarapplication.ui.components.ReservationCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.random.Random

@Composable
fun CarViewScreen(carID: String, adminView: String) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val car = remember { mutableStateOf<Car?>(null) }
    val reservations = remember { mutableStateOf<List<Reservation>>(emptyList()) }
    val statistics = remember { mutableStateOf<Map<String, Any>?>(null) }
    val carImages = TestValues.getFavoriteCars()
    val currentTime = LocalDateTime.now()


    fun isReservationWithinTwoHours(reservation: Reservation): Boolean {
        val twoHoursLater = currentTime.plusHours(2)
        return reservation.reservationStart.isAfter(currentTime) && reservation.reservationStart.isBefore(twoHoursLater)
    }

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            try {
                car.value = RetrofitClient.apiService.getCarByID(carID.toLong())
                reservations.value = RetrofitClient.apiService.getReservationsByCarID(car.value!!.carID)

            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                statistics.value = RetrofitClient.apiService.getStatisticsForCar(car.value!!.carID) as? Map<String, Any>
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()?:"Unknown Error"
                statistics.value = mapOf("statistics" to errorBody)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val isCarAvailable = reservations.value.none { isReservationWithinTwoHours(it) }

    car.value?.let {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                // Car details card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(5.dp)),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = it.carName,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 30.sp,
                            modifier = Modifier.padding(vertical = 8.dp),
                        )

                        HorizontalDivider(thickness = 2.dp, color = Color.Black)

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                painter = painterResource(id = carImages[Random.nextInt(0, 2)]),
                                contentDescription = "bitmap",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(75.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .padding(start = 18.dp)
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(18.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Brand:",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    )
                                    Text(text = it.brand, fontSize = 20.sp)
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Model:",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    )
                                    Text(text = it.model, fontSize = 20.sp)
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Availability:",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    )
                                    Text(
                                        text = if (isCarAvailable) "Available" else "Unavailable (next reservation within 2 hours)",
                                        fontSize = 20.sp,
                                        color = if (isCarAvailable) Color.Green else Color.Red
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Reservations Section
            item {
                // Updated Reservations Section
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(5.dp)),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Reservations",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 30.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )

                        HorizontalDivider(thickness = 2.dp, color = Color.Black)

                        if (reservations.value.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 250.dp) // Constrain height
                            ) {
                                LazyColumn {
                                    items(reservations.value) { reservation ->
                                        ReservationCard(reservation)
                                    }
                                }
                            }
                        } else {
                            Text(
                                text = "No reservations found",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            if (adminView.toBoolean()) {
                item {
                    // Statistics card
                    statistics.value?.let { stats ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp)
                                .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(5.dp)),
                            shape = RoundedCornerShape(5.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Top,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "Car Statistics",
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 30.sp,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                )

                                HorizontalDivider(thickness = 2.dp, color = Color.Black, modifier = Modifier.padding(bottom = 8.dp))

                                // Extract the statistics string
                                val statisticsString = stats["statistics"] as? String ?: ""
                                // Split the string into key-value pairs
                                val extractedStatistics = statisticsString.split("  ")
                                    .map { it.trim() } // Trim any extra spaces
                                    .mapNotNull { pair ->
                                        val splitPair = pair.split(":")
                                        if (splitPair.size == 2) {
                                            splitPair[0].trim() to splitPair[1].trim() // Return key-value pair
                                        } else null
                                    }

                                // Display each statistic
                                extractedStatistics.forEach { (key, value) ->
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 8.dp)
                                    ) {
                                        Text(
                                            text = key,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp,
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.padding(bottom = 4.dp)
                                        )
                                        Text(
                                            text = value,
                                            fontSize = 18.sp,
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    //Add reports
                    ReportScreen(carID.toLong())
                    }
            }
        }
    }
}

@Composable
fun ReportScreen(carID: Long) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // States for the reports
    val damageReports = remember { mutableStateListOf<DamageReport>() }
    val maintenanceReports = remember { mutableStateListOf<MaintenanceReport>() }

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                // Fetch reports from backend
                val allReports = RetrofitClient.apiService.getReportsByCar(carID)

                // Separate the reports by type
                damageReports.clear()
                maintenanceReports.clear()
                allReports.forEach { report ->
                    when (report) {
                        is DamageReport -> damageReports.add(report)
                        is MaintenanceReport -> maintenanceReports.add(report)
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to load reports: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Display the two columns
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Damage Reports", style = MaterialTheme.typography.headlineMedium)
        // Damage Reports Column
        LazyColumn(
            modifier = Modifier.padding(bottom = 16.dp).height(250.dp)
        ) {
            if (damageReports.size > 0) {
                items(damageReports) { report ->
                    ReportCard(report)
                }
            } else {
                item {
                    Text("No entries", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(vertical = 10.dp), color = Color.Gray)
                }
            }
        }
        Text("Maintenance Reports", style = MaterialTheme.typography.headlineLarge)
        // Maintenance Reports Column
        LazyColumn(
            modifier = Modifier.height(250.dp)
        ) {
            if (maintenanceReports.size > 0) {
                items(damageReports) { report ->
                    ReportCard(report)
                }
            } else {
                item {
                    Text("No entries", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(vertical = 10.dp), color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun ReportCard(report: Report) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Author: ${report.author_user?.name}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Date: ${report.date?.year}.${report.date?.monthValue}.${report.date?.dayOfMonth}", //Had to change that as the human readable somehow just crashed the app
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Description: ${report.description}",
                style = MaterialTheme.typography.bodyMedium
            )

            if (report is DamageReport) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Damage Details: ${report.damageDetails}",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else if (report is MaintenanceReport) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Maintenance Type: ${report.maintenanceType}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Cost: ${report.cost}€",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

fun formatIsoDateToHumanReadable(isoDateString: String): String {
    // Define a formatter that handles the format 'yyyy-MM-dd'T'HH:mm' (without seconds)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm", Locale.getDefault())

    // Parse the input date string into LocalDateTime
    val localDateTime = LocalDateTime.parse(isoDateString, formatter)

    // Define a human-readable format
    val humanReadableFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a", Locale.getDefault())

    // Return the formatted string
    return localDateTime.format(humanReadableFormatter)
}