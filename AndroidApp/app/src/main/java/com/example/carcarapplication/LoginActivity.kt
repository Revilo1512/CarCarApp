package com.example.carcarapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.carcarapplication.ui.screens.LoginScreen
import com.example.carcarapplication.ui.screens.RegisterScreen
import com.example.carcarapplication.ui.theme.CarCarApplicationTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarCarApplicationTheme {
                App()
            }
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(onNavigateToRegister = { navController.navigate("register") })
        }
        composable("register") {
            RegisterScreen(onNavigateToLogin = { navController.navigate("login") })
        }
    }
}