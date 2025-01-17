package com.example.carcarapplication.api_helpers

import com.example.carcarapplication.data_classes.DamageReport
import com.example.carcarapplication.data_classes.MaintenanceReport
import com.example.carcarapplication.data_classes.Report
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

class ReportAdapter(
    private val damageReportAdapter: JsonAdapter<DamageReport>,
    private val maintenanceReportAdapter: JsonAdapter<MaintenanceReport>
) : JsonAdapter<Report>() {

    override fun fromJson(reader: JsonReader): Report? {
        val json = reader.readJsonValue() as? Map<*, *>
            ?: throw IllegalArgumentException("Invalid JSON format")

        return when {
            json.containsKey("damageDetails") -> damageReportAdapter.fromJsonValue(json)
            json.containsKey("type") -> maintenanceReportAdapter.fromJsonValue(json)
            else -> throw IllegalArgumentException("Unknown Report type")
        }
    }

    override fun toJson(writer: JsonWriter, value: Report?) {
        when (value) {
            is DamageReport -> damageReportAdapter.toJson(writer, value)
            is MaintenanceReport -> maintenanceReportAdapter.toJson(writer, value)
            else -> throw IllegalArgumentException("Unknown Report subtype")
        }
    }
}
