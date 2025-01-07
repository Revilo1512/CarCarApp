package com.example.carcarapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.carcarapplication.TestValues.getCars
import com.example.carcarapplication.TestValues.getSingleGroup
import com.example.carcarapplication.ui.components.CarItem
import com.example.carcarapplication.ui.components.UserItem

@Composable
fun GroupScreen(groupName: String) {
    val cars = getCars()
    val group = getSingleGroup()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = groupName,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // CAR SECTION
        Text(
            text = "Cars",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyColumn (
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 500.dp)
        ){
            items(cars) { car ->
                CarItem(car = car)
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp),
            thickness = 1.dp,
            color = Color.Black
        )

        //MEMBER SECTION -- Should work once there is a user group in the testdata
        Text(
            text = "Members",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(group.members){user ->
                UserItem(user = user)
            }
        }
    }
}