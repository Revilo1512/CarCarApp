package com.example.carcarapplication.ui.navigation

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.carcarapplication.ui.screens.GroupScreen
import com.example.carcarapplication.ui.screens.HomeScreen
import com.example.carcarapplication.ui.components.DrawerContent
import kotlinx.coroutines.launch
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.carcarapplication.LoginActivity
import com.example.carcarapplication.R
import com.example.carcarapplication.TestValues
import com.example.carcarapplication.TestValues.getUser
import com.example.carcarapplication.api_helpers.RetrofitClient
import com.example.carcarapplication.data_classes.Group
import com.example.carcarapplication.ui.screens.UserSettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var groups by remember { mutableStateOf<List<Group>>(emptyList()) } // State for groups

    // Fetch groups asynchronously
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val fetchedGroups = RetrofitClient.apiService.getGroupsOfUser(RetrofitClient.getUser().userID)
                groups = fetchedGroups // Update groups state
            } catch (e: Exception) {
                e.printStackTrace() // Handle error if fetching groups fails
            }
        }
    }

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
                        onNavigateToGroup = { groupName ->
                            navController.navigate("group/$groupName")
                            scope.launch { drawerState.close() }
                        },
                        onNavigateToUserSettings = {
                            navController.navigate("user settings")
                            scope.launch { drawerState.close() }
                        },
                        onNavigateLogOut = {
                            scope.launch { drawerState.close() }

                            // Clear the cookies using the CookieInterceptor instance
                            RetrofitClient.cookieInterceptor.clearCookies()  // Make sure you have access to the interceptor

                            // Redirect the user to the login activity
                            val intent = Intent(context, LoginActivity::class.java).apply {}
                            context.startActivity(intent)
                        },
                        groups = groups // Pass the fetched groups
                    )
                }
            }
        ) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Image(
                                painter = painterResource(id = R.drawable.wordmark),
                                contentDescription = "Wordmark",
                                modifier = Modifier.size(120.dp)
                            )
                        },
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
                        HomeScreen()
                    }
                    composable("group/{groupName}") { backStackEntry ->
                        val groupName = backStackEntry.arguments?.getString("groupName") ?: "Unknown Group"
                        GroupScreen(groupName = groupName)
                    }
                    composable("user settings"){
                        UserSettingsScreen()
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AppNavigationPreview() {
    // Mock NavHostController for preview purposes
    val navController = rememberNavController()

    AppNavigation(navController = navController)
}
