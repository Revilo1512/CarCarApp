package com.example.carcarapplication.ui.screens

import android.app.AlertDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carcarapplication.api_helpers.RetrofitClient
import com.example.carcarapplication.data_classes.Car
import com.example.carcarapplication.data_classes.Group
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
fun GroupScreen(groupName: String, onNavigateToCarCreation: (String) -> Unit, onNavigateToCarView: (String) -> Unit) {

    val currentUser = RetrofitClient.getUser()
    val groupState = remember { mutableStateOf<Group?>(null) }
    val carsState = remember { mutableStateOf<List<Car>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            try {

                val groups = RetrofitClient.apiService.getGroupsOfUser(currentUser.userID)
                groupState.value = groups.find { it.name == groupName }

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

    fun reloadGroup() {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val updatedGroups = RetrofitClient.apiService.getGroupsOfUser(currentUser.userID)
                groupState.value = updatedGroups.find { it.name == groupName }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // UI
    val group = groupState.value
    val adminView = group?.let { isAdmin(it, currentUser) } == true

    if(showDialog && group != null) {
        AlertDialog(onDismiss = {showDialog = false}, groupID = group.groupID)
    }

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

        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Cars",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if(adminView) {
                Button(onClick = {
                    onNavigateToCarCreation(groupName)
                }) {
                    Text("+")
                }
            }
        }

        if (group != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
            ) {
                items(group.cars.orEmpty()) { car -> // Handle nullability
                    CarItem(
                        car = car,
                        currentTime = LocalDateTime.now(),
                        adminView = adminView,
                        onRemoveCar = {
                            coroutineScope.launch(Dispatchers.IO) {
                                try {
                                    val updatedGroup = RetrofitClient.apiService.removeCar(group.groupID, car.carID)
                                } catch (e: Exception) {
                                    e.printStackTrace() // Handle the error appropriately
                                }
                            }
                        },
                        onViewCar = {
                            onNavigateToCarView(car.carID.toString())
                        }
                    )
                }
            }
        }


        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp),
            thickness = 1.dp,
            color = Color.Black
        )

        // MEMBER SECTION
        if (group != null) {

            Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Members",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (adminView) {
                    Button(onClick = {
                        showDialog = true
                    }) {
                        Text("+")
                    }
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f, fill = false),
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
                                    val updatedGroup =
                                        RetrofitClient.apiService.getGroupById(group.groupID)

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

@Composable
fun AlertDialog(onDismiss: () -> Unit, groupID: Long) {
    var userID by remember { mutableStateOf("") }
    val pattern = remember { Regex("^\\d+\$") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    scope.launch {
                        try {
                            RetrofitClient.apiService.addUser(groupID = groupID, userID = userID.toLong())
                            Toast.makeText(context, "User added successfully", Toast.LENGTH_SHORT).show()
                            onDismiss() // Dismiss the dialog after successful addition
                        } catch (e: Exception) {
                            Log.d("Add User", e.toString())
                            Toast.makeText(context, "Failed to add user", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                enabled = userID.isNotEmpty() && userID.matches(pattern)
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(imageVector = Icons.Default.Person, contentDescription = "User")
                Text(text = "Add User", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            OutlinedTextField(
                value = userID,
                onValueChange = { userID = it },
                label = { Text("User ID") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        },
        modifier = Modifier.height(250.dp)
    )
}