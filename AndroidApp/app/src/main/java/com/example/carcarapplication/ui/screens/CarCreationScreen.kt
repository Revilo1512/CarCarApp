package com.example.carcarapplication.ui.screens

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
import androidx.compose.material3.ButtonColors
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carcarapplication.R
import com.example.carcarapplication.api_helpers.RetrofitClient
import com.example.carcarapplication.data_classes.Car
import com.example.carcarapplication.data_classes.Group
import kotlinx.coroutines.launch

@Composable
fun CarCreationScreen(groupName: String, onNavigateToGroup: (String) -> Unit) {
    var carName by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
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
            contentDescription = "bitmap",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(160.dp)
                .padding(bottom = 15.dp)
        )

        Text(
            text = "New Car",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp,
        )

        Text(
            text = groupName,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Light,
            fontSize = 15.sp,
            modifier = Modifier.padding(bottom = 20.dp),
        )

        OutlinedTextField(
            value = carName,
            onValueChange = { carName = it },
            label = { Text("Name") },
            singleLine = true
        )

        OutlinedTextField(
            value = brand,
            onValueChange = { brand = it },
            label = { Text("Brand") },
            singleLine = true
        )

        OutlinedTextField(
            value = model,
            onValueChange = { model = it },
            label = { Text("Model") },
            singleLine = true
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    onNavigateToGroup(groupName)
                },
                colors = ButtonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White,
                    disabledContentColor = Color.Gray,
                    disabledContainerColor = Color.LightGray
                ),
                enabled = true,
                modifier = Modifier.padding(top = 10.dp, start = 60.dp)
            ) {
                Text("Cancel")
            }

            Button(
                onClick = {
                    var newCar = Car(0, carName, brand, model, true)
                    try {
                        scope.launch {
                            newCar = RetrofitClient.apiService.createCar(newCar)
                            val groups =
                                RetrofitClient.apiService.getGroupsOfUser(RetrofitClient.getUser().userID)
                            val group = groups.find { group: Group -> group.name == groupName }
                            if (group != null) {
                                val updatedGroup =
                                    RetrofitClient.apiService.addCar(group.groupID, newCar.carID)
                            }
                        }
                    } catch (e: Exception) {
                        Log.d("CarCreation", e.toString())
                        Toast.makeText(context, "Creation failed", Toast.LENGTH_SHORT).show()
                    }
                    onNavigateToGroup(groupName)
                },
                colors = ButtonColors(
                    containerColor = Color.Green,
                    contentColor = Color.White,
                    disabledContentColor = Color.Gray,
                    disabledContainerColor = Color.LightGray
                ),
                enabled = carName != "" && brand != "" && model != "",
                modifier = Modifier.padding(top = 10.dp, end = 60.dp)
            ) {
                Text("Save")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarCreationPreview() {
    CarCreationScreen("group1", onNavigateToGroup = {})
}