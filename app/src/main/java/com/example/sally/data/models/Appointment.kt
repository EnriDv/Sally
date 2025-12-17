package com.example.sally.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Appointment(
    val id: Long = 0,

    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("user_id")
    val userId: String? = null,

    @SerialName("salon_name")
    val salonName: String,

    @SerialName("salon_address")
    val salonAddress: String,

    @SerialName("service_name")
    val serviceName: String,

    @SerialName("specialist_name")
    val specialistName: String,

    val price: String,
    @SerialName("appointment_date")
    val date: Long,

    val time: String,
    val status: String = "Active"
)