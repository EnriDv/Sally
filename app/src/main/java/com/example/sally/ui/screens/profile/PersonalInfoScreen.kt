package com.example.sally.ui.screens.profile

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.sally.ui.components.ProfileSubScreenLayout
import com.example.sally.ui.theme.GrayText

@Composable
fun PersonalInfoScreen(navController: NavController) {
    ProfileSubScreenLayout(
        navController = navController,
        title = "Información Personal"
    ) {
        Text("Aquí podrás editar tu nombre, foto y teléfono.", color = GrayText)
    }
}