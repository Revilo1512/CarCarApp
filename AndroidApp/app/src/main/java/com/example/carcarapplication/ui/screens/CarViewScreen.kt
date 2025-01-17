package com.example.carcarapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carcarapplication.R
import com.example.carcarapplication.TestValues
import com.example.carcarapplication.api_helpers.RetrofitClient
import com.example.carcarapplication.data_classes.Car
import com.example.carcarapplication.data_classes.Group
import com.example.carcarapplication.data_classes.Reservation
import com.example.carcarapplication.ui.components.ReservationCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun CarViewScreen(carID : String) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val car = remember { mutableStateOf<Car?>(null) }
    val reservations = remember { mutableStateOf<List<Reservation>>(emptyList()) }
    val carImages = TestValues.getFavoriteCars()

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            try {
                car.value = RetrofitClient.apiService.getCarByID(carID.toLong())
                reservations.value = RetrofitClient.apiService.getReservationsByCarID(car.value!!.carID)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    car.value?.let {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
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
                            painter = painterResource(id = carImages[Random.nextInt(0,2)]),
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
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(bottom = 16.dp),
                                )
                                Text(
                                    text = it.brand,
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.padding(bottom = 16.dp),
                                    fontSize = 20.sp,
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Model:",
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(bottom = 16.dp),
                                )
                                Text(
                                    text = it.model,
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.padding(bottom = 16.dp),
                                    fontSize = 20.sp,
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Availability:",
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(bottom = 16.dp),
                                )
                                Text(
                                    text = if (it.availabilityStatus) "Available" else "Not available",
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.padding(bottom = 16.dp),
                                    fontSize = 20.sp,
                                )
                            }
                        }
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(5.dp)),
                shape = RoundedCornerShape(5.dp)
            ) {
                Text(
                    text = "Reservations",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
                )

                HorizontalDivider(thickness = 2.dp, color = Color.Black)

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                ) {
                    items(reservations.value) { reservation ->
                        ReservationCard(reservation)
                    }
                }
            }
        }

    }
}