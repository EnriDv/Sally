package com.example.sally.data.models

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Specialist(
    val id: Long = 0,
    val name: String,
    val rating: Double = 5.0,

    @SerialName("color_hex")
    val colorHex: String = "#CCCCCC"
) {
    // Propiedad auxiliar para MVVM: Convierte Hex String a Color
    val color: Color
        get() = try {
            Color(android.graphics.Color.parseColor(colorHex))
        } catch (e: Exception) {
            Color.Gray
        }
}