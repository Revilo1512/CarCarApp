package com.example.carcarapplication.api_helpers

import com.example.carcarapplication.data_classes.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("user")
    suspend fun getUser(
        @Query("username") username: String,
        @Query("password") password: String
    ): User

    @POST("user")
    suspend fun postUser(
        @Body username: String,
        @Body email: String,
        @Body password: String
    ): User
}
