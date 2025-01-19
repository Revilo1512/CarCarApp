package com.example.carcarapplication.ui.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carcarapplication.R
import com.example.carcarapplication.api_helpers.RetrofitClient
import com.example.carcarapplication.data_classes.Group
import kotlinx.coroutines.launch

@Composable
fun GroupCreationScreen(
    onNavigateToHome: () -> Unit,
    updateGroups: suspend (Long, Context) -> List<Group>?
) {
    var groupName by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 80.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.carcarpicto),
            contentDescription = "Group Creation",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(160.dp)
                .padding(bottom = 15.dp)
        )

        Text(
            text = "Create New Group",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp,
        )

        OutlinedTextField(
            value = groupName,
            onValueChange = { groupName = it },
            label = { Text("Group Name") },
            singleLine = true
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    onNavigateToHome()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                ),
                modifier = Modifier.padding(top = 10.dp, start = 60.dp)
            ) {
                Text("Cancel")
            }

            Button(
                onClick = {
                    scope.launch {
                        try {
                            // Attempt to create the group
                            val createdGroup = RetrofitClient.apiService.createGroup(groupName)
                            //val updatedGroup = RetrofitClient.apiService.addUser(createdGroup.groupID, RetrofitClient.getUser().userID)

                            // After successful creation, update the list of groups and navigate home
                            // updateGroups(RetrofitClient.getUser().userID, context)
                            onNavigateToHome()

                        } catch (e: Exception) {
                            // Log the error and display a user-friendly message based on the exception type
                            Log.e("GroupCreation", "Error creating group", e)

                            when (e) {
                                is retrofit2.HttpException -> {
                                    // Retrofit HTTP exception
                                    Log.e("GroupCreation", "HTTP Error: ${e.code()} - ${e.message()}")
                                    if (e.code() == 500) {
                                        Toast.makeText(context, "Server Error: Unable to create group", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "HTTP Error: ${e.message()}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                is java.net.UnknownHostException -> {
                                    // No internet connection
                                    Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
                                }
                                else -> {
                                    // Handle any other unforeseen errors
                                    Toast.makeText(context, "An unexpected error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.White
                ),
                enabled = groupName.isNotEmpty(),
                modifier = Modifier.padding(top = 10.dp, end = 60.dp)
            ) {
                Text("Save")
            }
        }
    }
}