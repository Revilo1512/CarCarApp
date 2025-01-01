package com.example.carcarapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.carcarapplication.data_classes.User

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val user = intent.getSerializableExtra("user") as? User
            user?.let {
                MainScreen(it) // Passing the User object to your composable
            } ?: run {
                // Handle the case where the user is null (optional)
                Toast.makeText(this, "User data unavailable", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun MainScreen(user: User) {
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
