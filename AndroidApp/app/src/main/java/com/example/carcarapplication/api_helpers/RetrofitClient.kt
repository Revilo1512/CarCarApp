package com.example.carcarapplication.api_helpers

import com.example.carcarapplication.data_classes.DamageReport
import com.example.carcarapplication.data_classes.MaintenanceReport
import com.example.carcarapplication.data_classes.Report
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.0.30:8080/" // Emulator to localhost

    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(LocalDateTimeJsonAdapter()) // Register custom LocalDateTime adapter
            .add(
                Report::class.java,
                ReportAdapter(
                    damageReportAdapter = Moshi.Builder()
                        .add(LocalDateTimeJsonAdapter()) // Ensure nested adapters inherit this
                        .add(KotlinJsonAdapterFactory())
                        .build()
                        .adapter(DamageReport::class.java),
                    maintenanceReportAdapter = Moshi.Builder()
                        .add(LocalDateTimeJsonAdapter()) // Ensure nested adapters inherit this
                        .add(KotlinJsonAdapterFactory())
                        .build()
                        .adapter(MaintenanceReport::class.java)
                )
            )
            .add(KotlinJsonAdapterFactory()) // Kotlin reflective adapter
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi)) // Use Moshi with the registered adapters
            .build()
    }

    // Reusable instance of ApiService
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}