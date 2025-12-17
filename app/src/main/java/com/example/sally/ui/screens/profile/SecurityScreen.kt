package com.example.sally.ui.screens.profile

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.sally.ui.components.ProfileSubScreenLayout

@Composable
fun SecurityScreen(navController: NavController) {
    ProfileSubScreenLayout(navController, "Seguridad") {
        Text(
            text = "Gestión de cambio de contraseña y correo electrónico.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}