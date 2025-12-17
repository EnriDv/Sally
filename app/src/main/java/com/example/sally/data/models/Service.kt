package com.example.sally.data.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Service(
    val id: Long = 0, // ID opcional para inserciones nuevas
    val name: String,
    val price: String,

    // En la base de datos se llamará "icon_name" (ej: "face", "content_cut")
    @SerialName("icon_name")
    val iconName: String = "default"
) {
    // Propiedad auxiliar para MVVM: Convierte el String al Icono real de Compose
    val icon: ImageVector
        get() = when (iconName) {
            "content_cut" -> Icons.Default.ContentCut
            "palette" -> Icons.Default.Palette
            "spa" -> Icons.Default.Spa
            "face" -> Icons.Default.Face
            else -> Icons.Default.Star // Icono por defecto si no encuentra nombre
        }
}