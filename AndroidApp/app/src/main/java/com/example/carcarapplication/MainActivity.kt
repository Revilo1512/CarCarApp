package com.example.carcarapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.carcarapplication.ui.navigation.AppNavigation
import com.example.carcarapplication.ui.theme.CarCarApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarCarApplicationTheme {
                AppNavigation(navController = rememberNavController())
            }
        }
    }
}
