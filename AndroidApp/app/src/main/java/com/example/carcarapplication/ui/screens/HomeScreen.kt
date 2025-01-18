package com.example.carcarapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carcarapplication.TestValues.getFavoriteCars
import com.example.carcarapplication.api_helpers.RetrofitClient
import com.example.carcarapplication.data_classes.Reservation
import com.example.carcarapplication.data_classes.Trip
import com.example.carcarapplication.ui.components.CarCarouselItem
import com.example.carcarapplication.ui.components.ReservationItem
import com.example.carcarapplication.ui.components.TripItem
import com.example.carcarapplication.ui.utils.DriveState
import com.example.carcarapplication.ui.utils.formatElapsedTime
import kotlinx.coroutines.launch
import java.time.LocalTime

@Composable
fun HomeScreen(
    onNavigateToPreDrive: () -> Unit,
    onNavigateToDriveInfo: () -> Unit
) {
    val user = RetrofitClient.getUser()
    val greeting = getGreeting()
    //val trips = RetrofitClient.apiService.getTrips()
    val favoriteCars = getFavoriteCars()

    // State to manage the trips list and errors
    val tripsState = remember { mutableStateOf<List<Trip>?>(null) }

    //Trip Feature
    val isDriving = DriveState.isDriving.value
    val elapsedTime = remember { mutableStateOf("00:00") }
    val scope = rememberCoroutineScope()
    val reservations = remember { mutableStateOf<List<Reservation>>(emptyList()) }

    // Make the API call
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                reservations.value = RetrofitClient.apiService.getReservationForUser()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //Timer
    LaunchedEffect(isDriving) {
        while (isDriving) {
            val currentTime = System.currentTimeMillis()
            val duration = currentTime - DriveState.startTime.longValue // seconds
            elapsedTime.value = formatElapsedTime(duration)
            kotlinx.coroutines.delay(1000L) // Update every second
        }
    }

    // UI layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "${greeting}, ${user.name}!",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 20.dp, bottom = 35.dp).fillMaxWidth()
            )

            Text(
                text = "Your reservations",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp)
            ) {
                when {
                    reservations.value.isEmpty() -> {
                        item {
                            Text(
                                text = "This looks awfully empty...",
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .background(Color.LightGray)
                                    .fillParentMaxWidth()
                            )
                        }
                    }

                    else -> {
                        items(reservations.value) { reservation ->
                            ReservationItem(reservation = reservation)
                        }
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = 1.dp,
                color = Color.Black
            )

            // FAVOURITES SECTION
            Text(
                text = "Favorites (coming soon)",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .fillMaxWidth()
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(favoriteCars) { carImage ->
                    CarCarouselItem(carImage = carImage)
                }
            }
        }

        if (isDriving) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp)
                    .clickable { onNavigateToDriveInfo() }
            ) {
                Text(
                    text = "Active drive - Elapsed Time: ${elapsedTime.value}",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            FAB(
                onClick = {
                    onNavigateToPreDrive()
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(8.dp)
            )
        }
    }
}


//If we need that function more often we should move it to a "TimeFunctions.kt" file in the Utils folder
fun getGreeting(): String {
    val currentHour = LocalTime.now().hour
    return when (currentHour) {
        in 5..11 -> "Good morning"
        in 12..17 -> "Good afternoon"
        in 18..21 -> "Good evening"
        else -> "Good night"
    }
}

@Composable
fun FAB(onClick: () -> Unit, modifier: Modifier = Modifier) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.secondary,
        shape = CircleShape,
        modifier = modifier
    ) {
        Text(
            text = "Start Trip",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(10.dp)
        )
    }
}

@Preview
@Composable
fun ButtonPreview() {
    FAB(
        onClick = {}
    )
}

