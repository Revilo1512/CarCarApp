package com.example.carcarapplication.api_helpers

import com.example.carcarapplication.data_classes.Car
import com.example.carcarapplication.data_classes.DamageReport
import com.example.carcarapplication.data_classes.Group
import com.example.carcarapplication.data_classes.MaintenanceReport
import com.example.carcarapplication.data_classes.Report
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
import java.time.LocalDateTime

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

    @POST("cars/createCar")
    suspend fun createCar(
        @Body car: Car
    ): Car

    @GET("reservations/getReservationForUser")
    suspend fun getReservationForUser() : List<Reservation>

    //Trips
    @GET("trips")
    suspend fun getTrips(): List<Trip>

    @POST("trips/createTrip")
    suspend fun createTrip(
        @Query ("startTimeParam") startTime: String,
        @Query ("carID") carID: Long
    ): Trip

    @PUT("trips/modifyTrip/{tripID}")
    suspend fun modifyTrip(
        @Path("tripID") tripID: Long,
        @Query("distance") distance: Double,
        @Query("fuelUsed") fuelUsed: Double,
        @Query("endTimeParam") endTime: String
    ): Trip

    @POST("reservations/createReservation")
    suspend fun createReservation(
        @Query("carID") carID: Long,
        @Query("startTime") startTime: String,
        @Query("endTime") endTime: String
    ) : Reservation

    @GET("cars/getStatistics")
    suspend fun getStatisticsForCar(
        @Query("carId") carID: Long
    ) : Map<String, Any>

    @GET("reports/car/{carId}")
    suspend fun getReportsByCar(
        @Path("carId") carID: Long
    ) : List<Report>

    @POST("reports/createMaintenance")
    suspend fun createMaintenance(
        @Query("carId") carID: Long,
        @Query("tripId") tripID: Long,
        @Query("description") description: String,
        @Query("type") type: String,
        @Query("cost") cost: Double
    ) : MaintenanceReport

    @POST("reports/createDamage")
    suspend fun createDamage(
        @Query("carId") carID: Long,
        @Query("tripId") tripID: Long,
        @Query("description") description: String ,
        @Query("damageDetails")damageDetails: String
    ) : DamageReport

    @DELETE("reservations/cancelReservation")
    suspend fun cancelReservation(
        @Query("reservationID")reservationID: Long
    )
}
