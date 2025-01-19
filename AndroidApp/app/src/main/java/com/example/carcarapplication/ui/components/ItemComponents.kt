package com.example.carcarapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carcarapplication.TestValues
import com.example.carcarapplication.data_classes.Car
import com.example.carcarapplication.data_classes.Group
import com.example.carcarapplication.data_classes.Reservation
import com.example.carcarapplication.data_classes.Trip
import com.example.carcarapplication.data_classes.User
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
fun ReservationCard(reservation: Reservation) {
    val dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // User information
            Text(
                text = "User Name: ${reservation.user.name}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "Email: ${reservation.user.email}",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Reservation information
            Text(
                text = "Reservation Start: ${reservation.reservationStart.format(dateTimeFormatter)}",
                fontSize = 16.sp,
                color = Color.Black
            )
            Text(
                text = "Reservation End: ${reservation.reservationEnd.format(dateTimeFormatter)}",
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun CarItem(
    car: Car,
    currentTime: LocalDateTime,
    adminView: Boolean,
    onRemoveCar: (Car) -> Unit,
    onViewCar: (Car) -> Unit,
    onCreateReservation: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .clickable(onClick = { expanded = true })
            .border(
                width = 2.dp,
                //color = if (isAvailable(currentTime,car.reservations)) Color.Black else Color.Red,
                color = Color.Black,
                shape = MaterialTheme.shapes.medium
            ),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
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

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("View Details") },
                    onClick = {
                        expanded = false
                        onViewCar(car)
                    }
                )
                DropdownMenuItem(
                    text = { Text("Create Reservation") },
                    onClick = {
                        expanded = false
                        onCreateReservation()
                    }
                )
                if (adminView) {
                    DropdownMenuItem(
                        text = { Text("Remove Car") },
                        onClick = {
                            expanded = false
                            onRemoveCar(car)
                        }
                    )
                }
            }

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
                text = "${trip.startTime.format(formattedTime)} - ${
                    trip.endTime?.format(
                        formattedTime
                    )
                }",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

// Reservation Item Component
@Composable
fun ReservationItem(reservation: Reservation) {
    val formattedDate = DateTimeFormatter.ofPattern("EEE, dd MMM")
    val formattedTime = DateTimeFormatter.ofPattern("HH:mm")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp),
        shape = MaterialTheme.shapes.extraLarge,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Date Display
            Text(
                text = if (reservation.reservationStart.toLocalDate() == reservation.reservationEnd.toLocalDate()){
                        reservation.reservationStart.format(formattedDate)
                    } else {
                        "${reservation.reservationStart.format(formattedDate)} - ${reservation.reservationEnd.format(formattedDate)}"
                },
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )

            // Time Range Display
            Text(
                text = "${reservation.reservationStart.format(formattedTime)} - ${
                    reservation.reservationEnd.format(formattedTime)
                }",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            // User Name Display
            Text(
                text = "User: ${reservation.user.name}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )

            // Car Details Display
            Text(
                text = "Car: ${reservation.car.carName} (${reservation.car.brand})",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
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
    CarItem(
        car = car,
        currentTime = LocalDateTime.now(),
        adminView = true,
        onRemoveCar = {},
        onViewCar = {},
        onCreateReservation = {}
    )
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

// Preview for CarCarouselItem
@Preview(showBackground = true, backgroundColor = android.graphics.Color.GRAY.toLong())
@Composable
fun PreviewReservationItem() {
    val reservation: Reservation = Reservation(
        id = 1,
        user = TestValues.getUser(),
        car = TestValues.getCar(),
        reservationStart = LocalDateTime.now(),
        reservationEnd = LocalDateTime.now()
    )
    ReservationItem(reservation = reservation)
}