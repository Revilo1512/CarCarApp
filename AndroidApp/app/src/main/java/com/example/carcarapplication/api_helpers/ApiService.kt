package com.example.carcarapplication.api_helpers

import com.example.carcarapplication.data_classes.Car
import com.example.carcarapplication.data_classes.Group
import com.example.carcarapplication.data_classes.Reservation
import com.example.carcarapplication.data_classes.Trip
import com.example.carcarapplication.data_classes.User
import okhttp3.ResponseBody

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users/login")
    suspend fun getUser(
        @Query("email") email: String,
        @Query("password") password: String
    ): User

    @POST("users/register")
    suspend fun postUser(
        @Body user: User
    ): User

    @GET("groups/{groupID}")
    suspend fun getGroupById(
        @Path("groupID") groupID: Long
    ): Group

    @POST("groups/createGroup")
    suspend fun createGroup(
        @Query("groupName") groupName: String
    ): Group

    // Delete Group
    @DELETE("groups/deleteGroup")
    suspend fun deleteGroup(
        @Query("groupID") groupID: Long
    ): ResponseBody // Response will be a message (String)

    @GET("cars")
    fun getCars(): Call<List<Car>>

    // Add Car to Group
    @PUT("groups/addCar")
    suspend fun addCar(
        @Query("groupID") groupID: Long,
        @Query("carID") carID: Long
    ): Group

    // Remove Car from Group
    @PUT("groups/removeCar")
    suspend fun removeCar(
        @Query("groupID") groupID: Long,
        @Query("carID") carID: Long
    ): Group

    @GET("cars/{carID}")
    suspend fun getCarByID(
        @Path("carID") carID: Long
    ): Car

    @GET("cars/getReservations")
    suspend fun getReservationsByCarID(
        @Query("carId") carId: Long
    ): List<Reservation>

    // Remove Car from Group
    @PUT("groups/addUser")
    suspend fun addUser(
        @Query("groupID") groupID: Long,
        @Query("userID") userID: Long
    ): Group

    // Remove Car from Group
    @PUT("groups/removeUser")
    suspend fun removeUser(
        @Query("groupID") groupID: Long,
        @Query("userID") userID: Long
    ): Group

    @GET("groups/groupsOfUser")
    suspend fun getGroupsOfUser(
        @Query("userID") userID: Long
    ): List<Group>

    @PUT("groups/changeAdmin")
    suspend fun changeAdmin(
        @Query("groupID") groupID: Long,
        @Query("userID") userID: Long
    ): String

    @GET("trips")
    suspend fun getTrips(): List<Trip>

    @POST("cars/createCar")
    suspend fun createCar(
        @Body car: Car
    ): Car
}
