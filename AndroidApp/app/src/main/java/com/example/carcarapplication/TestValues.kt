package com.example.carcarapplication

import com.example.carcarapplication.data_classes.User

object TestValues {
    private val testUser = User(
        userID = 9999999,
        username = "testUser",
        email = "testuser@email.com",
        pendingInvites = listOf()
    )

    fun getUser(): User {
        return testUser
    }
}
