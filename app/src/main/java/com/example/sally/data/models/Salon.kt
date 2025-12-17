package com.example.sally.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.compose.ui.graphics.Color

@Serializable
data class Salon(
    val id: Long,
    val name: String,
    val address: String,
    val rating: Double = 0.0,

    // Agregamos esto para arreglar el error de SalonCard
    val reviews: Int = 0,

    @SerialName("image_url")
    val imageUrl: String? = null,

    val latitude: Double = 0.0,
    val longitude: Double = 0.0,

    @SerialName("is_closed")
    val isClosed: Boolean = false,

    @SerialName("cover_color")
    val coverColorHex: String = "#FFD7F2"
) {
    val coverColor: Color
        get() = try {
            Color(android.graphics.Color.parseColor(coverColorHex))
        } catch (e: Exception) {
            Color(0xFFFFD7F2)
        }

    val location: com.google.android.gms.maps.model.LatLng
        get() = com.google.android.gms.maps.model.LatLng(latitude, longitude)
}