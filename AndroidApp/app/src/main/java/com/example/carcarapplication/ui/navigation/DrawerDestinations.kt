package com.example.carcarapplication.ui.navigation

sealed class DrawerDestination(val route: String, val title: String) {
    object Home : DrawerDestination("home", "Home")
    object Group : DrawerDestination("group", "Group")
}