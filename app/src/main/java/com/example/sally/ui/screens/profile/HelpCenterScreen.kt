package com.example.sally.ui.screens.profile

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.sally.ui.components.ProfileSubScreenLayout

@Composable
fun HelpCenterScreen(navController: NavController) {
    ProfileSubScreenLayout(navController, "Centro de Ayuda") {
        Text(
            text = "Preguntas frecuentes y tutoriales.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}