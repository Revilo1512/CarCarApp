package com.example.carcarapplication.api_helpers

import CookieInterceptor
import com.example.carcarapplication.data_classes.DamageReport
import com.example.carcarapplication.data_classes.MaintenanceReport
import com.example.carcarapplication.data_classes.Report
import com.example.carcarapplication.data_classes.User
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.0.30:8080/"
    private lateinit var currentUser: User

    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(LocalDateTimeJsonAdapter())
            .add(
                Report::class.java,
                ReportAdapter(
                    damageReportAdapter = Moshi.Builder()
                        .add(LocalDateTimeJsonAdapter())
                        .add(KotlinJsonAdapterFactory())
                        .build()
                        .adapter(DamageReport::class.java),
                    maintenanceReportAdapter = Moshi.Builder()
                        .add(LocalDateTimeJsonAdapter())
                        .add(KotlinJsonAdapterFactory())
                        .build()
                        .adapter(MaintenanceReport::class.java)
                )
            )
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    val cookieInterceptor = CookieInterceptor()

    private val client = OkHttpClient.Builder()
        .addInterceptor(cookieInterceptor)
        .build()

    // Reusable instance of ApiService
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ApiService::class.java)
    }

    fun setUser(user: User) {
        currentUser = user
    }

    fun getUser(): User {
        return currentUser
    }


}