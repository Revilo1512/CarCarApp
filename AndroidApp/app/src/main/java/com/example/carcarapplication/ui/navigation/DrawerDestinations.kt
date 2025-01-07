package com.example.carcarapplication.ui.navigation

sealed class DrawerDestination(val route: String, val title: String) {
    object Home : DrawerDestination("home", "Home")
    object Group : DrawerDestination("group", "Group")
    object USettings : DrawerDestination("user settings", "User settings")
    object Logout : DrawerDestination("login", "Logout")
}