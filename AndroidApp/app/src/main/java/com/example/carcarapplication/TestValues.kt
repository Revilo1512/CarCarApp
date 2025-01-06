package com.example.carcarapplication

import com.example.carcarapplication.data_classes.Car
import com.example.carcarapplication.data_classes.Group
import com.example.carcarapplication.data_classes.Trip
import com.example.carcarapplication.data_classes.User
import java.time.LocalDateTime

object TestValues {
    private val testUser = User(
        userID = 9999999,
        username = "testUser",
        email = "testuser@email.com",
        pendingInvites = listOf()
    )

    private val testGroups = listOf(
        Group(
            groupID = 1,
            name = "Hawk Tours",
            admin = getUser(),
            members = listOf(getUser()),
            cars = listOf(getCar()),
            pendingRequests = emptyList()
        ),
        Group(
            groupID = 2,
            name = "Familie Schnürschuh",
            admin = getUser(),
            members = listOf(getUser()),
            cars = listOf(getCar()),
            pendingRequests = emptyList()
        )
    )
    
    private val testCar = Car(
        carID = 1,
        carName = "McQueen",
        brand = "Fast",
        model = "Quickest",
        availabilityStatus = true,
        reports = emptyList(),
        trips = getTrips()
    )
    

    private val testTrips = listOf(
        Trip(
            tripID = 1,
            startTime = LocalDateTime.of(2025, 1, 5, 9, 45),
            endTime = LocalDateTime.of(2025, 1, 5, 13, 0),
            distance = 10.5,
            fuelUsed = 1.4
        ),
        Trip(
            tripID = 2,
            startTime = LocalDateTime.of(2025, 1, 6, 9, 45),
            endTime = LocalDateTime.of(2025, 1, 6, 15, 0),
            distance = 10.5,
            fuelUsed = 1.4
        ),
        Trip(
            tripID = 3,
            startTime = LocalDateTime.of(2025, 1, 7, 9, 45),
            endTime = LocalDateTime.of(2025, 1, 7, 13, 0),
            distance = 10.5,
            fuelUsed = 1.4
        ),
        Trip(
            tripID = 4,
            startTime = LocalDateTime.of(2025, 1, 8, 9, 45),
            endTime = LocalDateTime.of(2025, 1, 8, 13, 0),
            distance = 10.5,
            fuelUsed = 1.4
        )
    )

    //These testvalues presumebly will end up in the whole "TestCar" data
    private val favoriteCars = listOf(
        R.drawable.car1,
        R.drawable.car2,
        R.drawable.car3
    )

    fun getUser(): User {
        return testUser
    }
    
    fun getGroups(): List<Group> {
        return testGroups
    }

    fun getCar(): Car {
        return testCar
    }
    
    fun getTrips(): List<Trip> {
        return testTrips
    }

    fun getFavoriteCars(): List<Int> {
        return favoriteCars
    }
}

