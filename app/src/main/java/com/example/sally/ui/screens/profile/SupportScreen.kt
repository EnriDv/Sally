package com.example.sally.ui.screens.profile

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.sally.ui.components.ProfileSubScreenLayout
import com.example.sally.ui.theme.GrayText

@Composable
fun SupportScreen(navController: NavController) {
    ProfileSubScreenLayout(navController, "Soporte Técnico") {
        Text("Formulario de contacto con soporte.", color = GrayText)
    }
}