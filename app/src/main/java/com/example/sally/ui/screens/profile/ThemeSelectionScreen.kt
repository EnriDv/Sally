package com.example.sally.ui.screens.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sally.ui.components.ProfileSubScreenLayout
import com.example.sally.ui.theme.PurpleStart
import com.example.sally.utils.AppThemeMode
import com.example.sally.utils.ThemeManager

@Composable
fun ThemeSelectionScreen(navController: NavController) {
    val currentMode by ThemeManager.themeMode.collectAsState()

    ProfileSubScreenLayout(navController, "Apariencia") {
        Text("Elige el tema de la aplicación", color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(16.dp))

        ThemeOptionItem("Tema del Sistema", AppThemeMode.SYSTEM, currentMode)
        HorizontalDivider()
        ThemeOptionItem("Modo Claro", AppThemeMode.LIGHT, currentMode)
        HorizontalDivider()
        ThemeOptionItem("Modo Oscuro", AppThemeMode.DARK, currentMode)
    }
}

@Composable
fun ThemeOptionItem(text: String, mode: AppThemeMode, currentMode: AppThemeMode) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { ThemeManager.saveTheme(mode) }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
        if (mode == currentMode) {
            Icon(Icons.Default.Check, contentDescription = null, tint = PurpleStart)
        }
    }
}