package com.example.carcarapplication.ui.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.carcarapplication.data_classes.DamageReport
import com.example.carcarapplication.data_classes.MaintenanceReport

object ReportState {
    var selectedReportType: MutableState<String> = mutableStateOf("No Report")

    var damageReport: MutableState<DamageReport> = mutableStateOf(DamageReport())
    var maintenanceReport: MutableState<MaintenanceReport> = mutableStateOf(MaintenanceReport())
}