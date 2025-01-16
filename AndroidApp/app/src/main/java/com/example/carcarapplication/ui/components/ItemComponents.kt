package com.example.carcarapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.carcarapplication.TestValues
import com.example.carcarapplication.data_classes.Car
import com.example.carcarapplication.data_classes.Group
import com.example.carcarapplication.data_classes.Trip
import com.example.carcarapplication.data_classes.User
import com.example.carcarapplication.ui.utils.isAvailable
import com.example.carcarapplication.ui.utils.promoteToAdmin
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

//This file contains Items used in columns for the Home and Group Screen

@Composable
fun UserItem(
    user: User,
    group: Group,
    adminView: Boolean,
    isAdmin: Boolean,
    onPromoteToAdmin: (User) -> Unit,
    onRemoveUser: (User) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .clickable(onClick = { expanded = true }),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.titleSmall
                    )
                    if (isAdmin) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Admin Star"
                        )
                    }
                }
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (adminView && !isAdmin) { // Show options if the current user is an admin and the user is not an admin
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Promote to Admin") },
                        onClick = {
                            expanded = false
                            onPromoteToAdmin(user)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Remove User") },
                        onClick = {
                            expanded = false
                            onRemoveUser(user)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CarItem(
    car: Car,
    currentTime: LocalDateTime
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .border(
                width = 2.dp,
                //color = if (isAvailable(currentTime,car.reservations)) Color.Black else Color.Red,
                color = Color.Black,
                shape = MaterialTheme.shapes.medium
            ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = car.carName,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = "${car.brand} ${car.model}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = if (car.availabilityStatus) "Avaiable" else "Unavaiable",
                style = MaterialTheme.typography.bodyMedium
            )
        }
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

// Preview for UserItem
@Preview(showBackground = true)
@Composable
fun PreviewUserItem() {
    val user: User = TestValues.getUser()
    val group: Group = TestValues.getSingleGroup()
    UserItem(
        user = user,
        group = group,
        adminView = true,
        isAdmin = true,
        onPromoteToAdmin = {},
        onRemoveUser = {}
    )
}

// Preview for CarItem
@Preview(showBackground = true)
@Composable
fun PreviewCarItem() {
    val car: Car = TestValues.getCar()
    CarItem(car = car, currentTime = LocalDateTime.now())
}

// Preview for TripItem
@Preview(showBackground = true)
@Composable
fun PreviewTripItem() {
    val trip: Trip = TestValues.getTrips().first()
    TripItem(trip = trip)
}

// Preview for CarCarouselItem
@Preview(showBackground = true)
@Composable
fun PreviewCarCarouselItem() {
    val carImage: Int = TestValues.getFavoriteCars().first()
    CarCarouselItem(carImage = carImage)
}