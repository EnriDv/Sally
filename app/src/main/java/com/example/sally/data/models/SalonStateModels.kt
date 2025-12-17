package com.example.sally.data.models

import androidx.compose.ui.graphics.Color

// --- ESTADOS DEL SALÓN (UI) ---
data class OpenState(
    override val contentAlpha: Float = 1f,
    override val isActionEnabled: Boolean = true,
    override val actionButtonText: String = "Reservar Ahora",
    override val actionButtonColor: Color = Color(0xFF9810FA),
    override val isDialogVisible: Boolean = false
) : SalonStateBehavior

data class ClosedState(
    override val contentAlpha: Float = 0.5f,
    override val isActionEnabled: Boolean = false,
    override val actionButtonText: String = "Cerrado",
    override val actionButtonColor: Color = Color.Gray,
    override val isDialogVisible: Boolean = true
) : SalonStateBehavior

interface SalonStateBehavior {
    val contentAlpha: Float
    val isActionEnabled: Boolean
    val actionButtonText: String
    val actionButtonColor: Color
    val isDialogVisible: Boolean
}

// --- MOCKS ACTUALIZADOS (Usando los nuevos modelos serializables) ---

val mockSalons = listOf(
    Salon(id = 1L, name = "Beauty Center", address = "Av. Banzer", rating = 4.8, reviews = 120, coverColorHex = "#FFD7F2"),
    Salon(id = 2L, name = "Glamour Spa", address = "Equipetrol", rating = 4.5, reviews = 85, coverColorHex = "#E1BEE7", isClosed = true)
)

val mockServices = listOf(
    Service(name = "Corte", price = "$25", iconName = "content_cut"),
    Service(name = "Manicure", price = "$15", iconName = "palette"),
    Service(name = "Pedicure", price = "$20", iconName = "spa")
)

val mockSpecialists = listOf(
    Specialist(name = "Ana García", rating = 4.8, colorHex = "#FFCDD2"),
    Specialist(name = "Carlos Ruiz", rating = 4.5, colorHex = "#BBDEFB")
)