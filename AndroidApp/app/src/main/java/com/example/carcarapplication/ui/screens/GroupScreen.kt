package com.example.carcarapplication.ui.screens

import android.widget.Toast
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.carcarapplication.api_helpers.RetrofitClient
import com.example.carcarapplication.data_classes.Car
import com.example.carcarapplication.data_classes.Group
import com.example.carcarapplication.data_classes.User
import com.example.carcarapplication.ui.components.CarItem
import com.example.carcarapplication.ui.components.UserItem
import com.example.carcarapplication.ui.utils.isAdmin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime

@Composable
fun GroupScreen(groupName: String) {
    // State to hold the group and cars data
    val currentUser = RetrofitClient.getUser()
    val groupState = remember { mutableStateOf<Group?>(null) }
    val carsState = remember { mutableStateOf<List<Car>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Fetch group and cars when the composable is first displayed
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                // Fetch groups and filter for the one with the specified groupName
                val groups = RetrofitClient.apiService.getGroupsOfUser(currentUser.userID)
                groupState.value = groups.find { it.name == groupName }

                // Fetch cars using enqueue method for asynchronous call
                RetrofitClient.apiService.getCars().enqueue(object : Callback<List<Car>> {
                    override fun onResponse(call: Call<List<Car>>, response: Response<List<Car>>) {
                        if (response.isSuccessful) {
                            carsState.value = response.body() ?: emptyList()
                        } else {
                            // Handle the case where the response is not successful
                            carsState.value = emptyList()
                        }
                    }

                    override fun onFailure(call: Call<List<Car>>, t: Throwable) {
                        // Handle the error case
                        carsState.value = emptyList()
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace() // Handle the error appropriately
            }
        }
    }

    // Function to reload the group after an update
    fun reloadGroup() {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val updatedGroups = RetrofitClient.apiService.getGroupsOfUser(currentUser.userID)
                groupState.value = updatedGroups.find { it.name == groupName }
            } catch (e: Exception) {
                e.printStackTrace() // Handle the error appropriately
            }
        }
    }

    // UI
    val group = groupState.value
    val adminView = group?.let { isAdmin(it, currentUser) } == true

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = groupName,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // CAR SECTION
        Text(
            text = "Cars",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Make cars scrollable if more than 3 items
        val isScrollable = carsState.value.size > 3
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false)
        ) {
            items(carsState.value) { car ->
                CarItem(car = car, currentTime = LocalDateTime.now())
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp),
            thickness = 1.dp,
            color = Color.Black
        )

        // MEMBER SECTION
        if (group != null) {
            Text(
                text = "Members",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
            ) {
                items(group.users) { user ->
                    UserItem(
                        user = user,
                        group = group,
                        adminView = adminView,
                        isAdmin = isAdmin(group, user),
                        onPromoteToAdmin = {
                            coroutineScope.launch(Dispatchers.IO) {
                                try {
                                    // Call the suspend function directly
                                    val feedback = RetrofitClient.apiService.changeAdmin(
                                        groupID = group.groupID,
                                        userID = user.userID
                                    )
                                    Toast.makeText(context, feedback, Toast.LENGTH_SHORT).show()
                                    val updatedGroup = RetrofitClient.apiService.getGroupById(group.groupID)

                                    groupState.value = updatedGroup // Update UI with new group
                                } catch (e: Exception) {
                                    e.printStackTrace() // Handle the error appropriately
                                }
                            }
                        },
                        onRemoveUser = {
                            coroutineScope.launch(Dispatchers.IO) {
                                try {
                                    val updatedGroup = RetrofitClient.apiService.removeUser(
                                        groupID = group.groupID,
                                        userID = user.userID
                                    )
                                    groupState.value = updatedGroup // Update UI with new group
                                } catch (e: Exception) {
                                    e.printStackTrace() // Handle the error appropriately
                                }
                            }
                        }
                    )
                }
            }
        } else {
            Text(
                text = "Group not found.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Red,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}
