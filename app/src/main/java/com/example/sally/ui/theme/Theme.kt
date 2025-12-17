package com.example.sally.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PurpleDarkAccent,      // Púrpura suave
    onPrimary = Color.Black,         // Texto sobre botón púrpura
    secondary = PinkDarkAccent,      // Rosa suave
    onSecondary = Color.Black,
    tertiary = Pink80,
    background = DarkBackground,     // Tu gris oscuro (#121212)
    surface = DarkSurface,           // Tu superficie oscura (#1E1E1E)
    onBackground = DarkText,         // Texto blanco suave
    onSurface = DarkText
)

private val LightColorScheme = lightColorScheme(
    primary = PurpleStart,
    onPrimary = Color.White,
    secondary = PinkEnd,
    onSecondary = Color.White,
    tertiary = Pink40,
    background = BackgroundColor,
    surface = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun SallyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}