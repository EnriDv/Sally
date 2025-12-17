package com.example.sally.ui.screens.profile

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.sally.ui.components.ProfileSubScreenLayout

@Composable
fun PrivacyScreen(navController: NavController) {
    ProfileSubScreenLayout(navController, "Privacidad") {
        Text(
            text = "Políticas de privacidad y gestión de datos.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}