package com.example.carcarapplication.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.carcarapplication.TestValues.getFavoriteCars
import com.example.carcarapplication.api_helpers.RetrofitClient
import com.example.carcarapplication.data_classes.Trip
import com.example.carcarapplication.ui.components.CarCarouselItem
import com.example.carcarapplication.ui.components.TripItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalTime

@Composable
fun HomeScreen() {
    val user = RetrofitClient.getUser()
    val greeting = getGreeting()
    //val trips = RetrofitClient.apiService.getTrips()
    val favoriteCars = getFavoriteCars()

    // State to manage the trips list and errors
    val tripsState = remember { mutableStateOf<List<Trip>?>(null) }
    val errorState = remember { mutableStateOf(false) }

    // Make the API call
    LaunchedEffect(Unit) {
        val call: Call<List<Trip>> = RetrofitClient.apiService.getTrips()
        call.enqueue(object : Callback<List<Trip>> {
            override fun onResponse(call: Call<List<Trip>>, response: Response<List<Trip>>) {
                if (response.isSuccessful) {
                    tripsState.value = response.body() // Update trips state
                } else {
                    tripsState.value = emptyList() // Handle empty response gracefully
                }
            }

            override fun onFailure(call: Call<List<Trip>>, t: Throwable) {
                errorState.value = true // Set error state on failure
                Log.d("error api", t.toString())
            }
        })
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
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // SCHEDULE SECTION
            Text(
                text = "Your upcoming trips",
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
                    .heightIn(max = 500.dp)
            ) {
                when {
                    errorState.value -> {
                        item {
                            Text(
                                text = "An error occurred. Please try again.",
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .background(Color.LightGray)
                            )
                        }
                    }

                    tripsState.value.isNullOrEmpty() -> {
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
                        items(tripsState.value!!.take(4)) { trip ->
                            TripItem(trip = trip)
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
                text = "Favourites",
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

        FAB(
            onClick = {},
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
        )
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
    LargeFloatingActionButton(
        onClick = {},
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.secondary,
        shape = CircleShape,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            "Small floating action button.",
            modifier = Modifier.size(64.dp)
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

