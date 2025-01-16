package com.example.carcarapplication.ui.screens

import DriveState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.carcarapplication.api_helpers.RetrofitClient
import com.example.carcarapplication.data_classes.Car
import com.example.carcarapplication.data_classes.Group
import com.example.carcarapplication.ui.components.DropdownMenuWithItems
import kotlinx.coroutines.launch

@Composable
fun PreDriveScreen(
    onNavigateToHome: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var groups by remember { mutableStateOf<List<Group>>(emptyList()) }
    var selectedGroup by remember { mutableStateOf<Group?>(null) }
    var cars by remember { mutableStateOf<List<Car>>(emptyList()) }
    var selectedCar by remember { mutableStateOf<Car?>(null) }

    // Fetch groups
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                groups = RetrofitClient.apiService.getGroupsOfUser(RetrofitClient.getUser().userID)
            } catch (e: Exception) {
                e.printStackTrace() // Handle error
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Group Dropdown
        DropdownMenuWithItems(
            label = "Select Group",
            items = groups.map { it.name },
            selectedItem = selectedGroup?.name ?: "Select a group",
            onItemSelected = { groupName ->
                selectedGroup = groups.find { it.name == groupName}
                cars = selectedGroup?.cars ?: listOf()
                    // Fetch cars for the selected group
                scope.launch {
                    try {
                        val groupDetails = RetrofitClient.apiService.getGroupById(selectedGroup!!.groupID)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        )

        // Car Dropdown
        DropdownMenuWithItems(
            label = "Select Car",
            items = if (cars.isEmpty()) listOf("No cars available") else cars.map { it.carName }, // Show placeholder if empty
            selectedItem = selectedCar?.carName ?: "Select a car",
            onItemSelected = { carName ->
                selectedCar = cars.find { it.carName == carName } // Match selected car by name
            }
        )

        // Start Drive Button
        Button(
            onClick = {
                DriveState.isDriving.value = true
                DriveState.startTime.longValue = System.currentTimeMillis()
                onNavigateToHome()
            },
            enabled = selectedGroup != null && selectedCar != null
        ) {
            Text("Start Drive")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreDrivePreView() {
    PreDriveScreen(
        onNavigateToHome = {}
    )
}