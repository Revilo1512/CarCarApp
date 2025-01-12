package com.example.carcarapplication

import android.os.Bundle
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

            @Suppress("DEPRECATION")
            val user = intent.getSerializableExtra("user") as User

            AppNavigation(navController = navController, user = user)
        }
    }
}
