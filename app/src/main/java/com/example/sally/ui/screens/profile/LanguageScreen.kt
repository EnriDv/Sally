package com.example.sally.ui.screens.profile

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.sally.ui.components.ProfileSubScreenLayout

@Composable
fun LanguageScreen(navController: NavController) {
    ProfileSubScreenLayout(navController, "Idioma") {
        Text(
            text = "Selecciona el idioma de la aplicación.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}