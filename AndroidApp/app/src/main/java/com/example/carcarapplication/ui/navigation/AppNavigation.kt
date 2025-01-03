package com.example.carcarapplication.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.carcarapplication.data_classes.User
import com.example.carcarapplication.ui.GroupScreen
import com.example.carcarapplication.ui.HomeScreen
import com.example.carcarapplication.ui.components.DrawerContent
import kotlinx.coroutines.launch
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(navController: NavHostController, user: User) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    Box {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(end = 80.dp)
                        .background(Color.White) // Background only for the drawer content
                ) {
                    DrawerContent(
                        onNavigateToHome = {
                            navController.navigate("home")
                            scope.launch { drawerState.close() }
                        },
                        onNavigateToGroup = {
                            navController.navigate("group")
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("CarCar Application") },
                        navigationIcon = {
                            IconButton(
                                onClick = { scope.launch { drawerState.open() } }
                            ) {
                                Icon(
                                    Icons.Default.Menu,
                                    contentDescription = "Menu"
                                )
                            }
                        }
                    )
                }
            ) { padding ->
                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier.padding(padding)
                ) {
                    composable("home") {
                        HomeScreen(user = user)
                    }
                    composable("group") {
                        GroupScreen()
                    }
                }
            }
        }
    }
}

