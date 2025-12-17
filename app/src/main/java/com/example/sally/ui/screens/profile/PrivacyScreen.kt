package com.example.sally.ui.screens.profile

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.sally.ui.components.ProfileSubScreenLayout
import com.example.sally.ui.theme.GrayText

@Composable
fun PrivacyScreen(navController: NavController) {
    ProfileSubScreenLayout(navController, "Privacidad") {
        Text("Políticas de privacidad y gestión de datos.", color = GrayText)
    }
}