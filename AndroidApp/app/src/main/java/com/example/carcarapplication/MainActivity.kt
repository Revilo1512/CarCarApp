package com.example.carcarapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.carcarapplication.data_classes.User
import com.example.carcarapplication.ui.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val user = intent.getSerializableExtra("user") as? User

            user?.let {
                AppNavigation(navController = navController, user = user)
            } ?: run {
                Toast.makeText(this, "User data unavailable", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
