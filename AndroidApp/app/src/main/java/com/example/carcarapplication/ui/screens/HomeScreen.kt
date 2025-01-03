package com.example.carcarapplication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.carcarapplication.data_classes.User

@Composable
fun HomeScreen(user: User) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Welcome, ${user.username}!", style = MaterialTheme.typography.headlineLarge)
        Text(text = "Email: ${user.email}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "ID: ${user.userID}", style = MaterialTheme.typography.bodyMedium)
    }
}