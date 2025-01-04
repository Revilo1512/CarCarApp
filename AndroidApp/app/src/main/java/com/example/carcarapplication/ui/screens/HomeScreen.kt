package com.example.carcarapplication.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.carcarapplication.TestValues.getFavoriteCars
import com.example.carcarapplication.TestValues.getTrips
import com.example.carcarapplication.data_classes.Trip
import com.example.carcarapplication.data_classes.User
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(user: User) {
    val greeting = getGreeting()
    val trips = getTrips()
    val favoriteCars = getFavoriteCars()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "$greeting, ${user.username}!",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // SCHEDULE SECTION
        Text(
            text = "Your trips",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyColumn (
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp)
        ){
            items(trips.take(4)) { trip ->
                TripItem(trip = trip)
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp),
            thickness = 1.dp,
            color = Color.Black
        )

        //FAVE SECTION
        Text(
            text = "Favourites",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(favoriteCars) { carImage ->
                CarCarouselItem(carImage = carImage)
            }
        }
        //Text(text = "Welcome, ${user.username}!", style = MaterialTheme.typography.headlineLarge)
        //Text(text = "Email: ${user.email}", style = MaterialTheme.typography.bodyMedium)
        //Text(text = "ID: ${user.userID}", style = MaterialTheme.typography.bodyMedium)
    }
}

//If we need that function more often we should move it to a "TimeFunctions.kt" file in the Utils folder
fun getGreeting(): String {
    val currentHour = LocalTime.now().hour
    return when {
        currentHour in 5..11 -> "Good morning"
        currentHour in 12..17 -> "Good afternoon"
        currentHour in 18..21 -> "Good evening"
        else -> "Good night"
    }
}

// Schedule Item Component
@Composable
fun TripItem(trip: Trip) {
    val formattedDate = DateTimeFormatter.ofPattern("EEE, dd MMM")
    val formattedTime = DateTimeFormatter.ofPattern("HH:mm")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = trip.startTime.format(formattedDate),
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = "${trip.startTime.format(formattedTime)} - ${trip.endTime.format(formattedTime)}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

// Carousel Item Component [Still have to properly convert this into a Carousel tbh]
@Composable
fun CarCarouselItem(carImage: Int) {
    Card(
        modifier = Modifier.size(120.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Image(
            painter = painterResource(id = carImage),
            contentDescription = "Favorite Car",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

