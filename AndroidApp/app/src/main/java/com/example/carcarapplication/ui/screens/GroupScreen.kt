package com.example.carcarapplication.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.carcarapplication.data_classes.User
import com.example.carcarapplication.ui.components.CarItem
import com.example.carcarapplication.ui.components.UserItem
import com.example.carcarapplication.ui.utils.isAdmin
import java.time.LocalDateTime

@Composable
fun GroupScreen(groupName: String, currentUser: User) {
    val cars = getCars()
    val group = getSingleGroup()
    val adminView = isAdmin(group,currentUser) //Is the current user the GroupAdmin?

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
                CarItem(car = car, currentTime = LocalDateTime.now())
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

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(group.members){user ->
                UserItem(
                    user = user,
                    group = group,
                    adminView = adminView,
                    isAdmin = isAdmin(group,user),
                    onPromoteToAdmin = {},
                    onRemoveUser = {}
                )
            }
        }
    }
}