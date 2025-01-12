package com.example.carcarapplication

import com.example.carcarapplication.data_classes.Car
import com.example.carcarapplication.data_classes.Group
import com.example.carcarapplication.data_classes.Trip
import com.example.carcarapplication.data_classes.User
import java.time.LocalDateTime

object TestValues {
    private val testUser = User(
        userID = 9999999,
        name = "testUser",
        email = "testuser@email.com",
        password = "123",
        //pendingInvites = listOf()
    )

    private val testMembers = listOf(
        User(
            userID = 1,
            name = "Alex",
            email = "Alex@email.com",
            password = "123",
            //pendingInvites = listOf()
        ),
        User(
            userID = 2,
            name = "Greg",
            email = "Greg@email.com",
            password = "password"
            //groups = emptyList()
            //pendingInvites = listOf()
        ),
        User(
            userID = 3,
            name = "Jeremy",
            email = "Jeremy@email.com",
            password = "password"
            //groups = emptyList()
            //pendingInvites = listOf()
        )
    )

    private fun getMembers(): List<User>{
        return testMembers
    }

    private val testGroups = listOf(
        Group(
            id = 1,
            name = "Hawk Tours",
            admin = getMembers()[0],
            users = getMembers(),
            cars = listOf(getCar()),
            //pendingRequests = emptyList()
        ),
        Group(
            id = 2,
            name = "Familie Schnürschuh",
            admin = getUser(),
            users = listOf(getUser()),
            cars = listOf(getCar()),
            //pendingRequests = emptyList()
        )
    )

    private val testCar = Car(
        id = 1,
        carName = "McQueen",
        brand = "Fast",
        model = "Quickest",
        availabilityStatus = true,
        reservations = emptyList()
        //reports = emptyList(),
        //trips = getTrips()
    )

    private val testCars = listOf(
        Car(
            id = 1,
            carName = "McQueen",
            brand = "Fast",
            model = "Quickest",
            availabilityStatus = true,
            reservations = emptyList()
            //reports = emptyList(),
            //trips = getTrips()
        ),
        Car(
            id = 2,
            carName = "Reidiga Ford",
            brand = "Ford",
            model = "Galaxy",
            availabilityStatus = false,
            reservations = emptyList()
            //reports = emptyList(),
            //trips = getTrips()
        ),
        Car(
            id = 3,
            carName = "Bathmorgihni",
            brand = "Lambo",
            model = "Showerhead",
            availabilityStatus = true,
            reservations = emptyList()
            //reports = emptyList(),
            //trips = getTrips()
        )
    )
    

    private val testTrips = listOf(
        Trip(
            tripID = 1,
            startTime = LocalDateTime.of(2025, 1, 11, 20, 0),
            endTime = LocalDateTime.of(2025, 1, 11, 22, 0),
            distance = 10.5,
            fuelUsed = 1.4,
            car = testCar,
            user = testUser
        ),
        Trip(
            tripID = 2,
            startTime = LocalDateTime.of(2025, 1, 12, 9, 45),
            endTime = LocalDateTime.of(2025, 1, 12, 15, 0),
            distance = 10.5,
            fuelUsed = 1.4,
            car = testCar,
            user = testUser
        ),
        Trip(
            tripID = 3,
            startTime = LocalDateTime.of(2025, 1, 7, 9, 45),
            endTime = LocalDateTime.of(2025, 1, 7, 13, 0),
            distance = 10.5,
            fuelUsed = 1.4,
            car = testCar,
            user = testUser
        ),
        Trip(
            tripID = 4,
            startTime = LocalDateTime.of(2025, 1, 8, 9, 45),
            endTime = LocalDateTime.of(2025, 1, 8, 13, 0),
            distance = 10.5,
            fuelUsed = 1.4,
            car = testCar,
            user = testUser
        )
    )

    //These testvalues presumebly will end up in the whole "TestCar" data


    fun getUser(): User {
        return testUser
    }

    private fun getUsers(): List<User> {
        return testMembers
    }

    fun getGroups(): List<Group> {
        return testGroups
    }

    fun getSingleGroup(): Group{
        val groups = getGroups()
        return groups[0]
    }

    fun getCar(): Car {
        return testCar
    }

    fun getCars(): List<Car> {
        return testCars
    }
    
    fun getTrips(): List<Trip> {
        return testTrips
    }

    fun getFavoriteCars(): List<Int> {
        return listOf(R.drawable.car1, R.drawable.car2, R.drawable.car3)
    }
}

