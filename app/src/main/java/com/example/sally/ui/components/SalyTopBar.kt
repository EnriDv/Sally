package com.example.sally.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalyTopBar(onMenuClick: () -> Unit) {
    TopAppBar(
        title = {
            Text("Saly", fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, contentDescription = null)
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(Icons.Default.Search, contentDescription = null)
            }
            IconButton(onClick = {}) {
                BadgedBox(badge = { Badge { Text("1") } }) {
                    Icon(Icons.Default.Notifications, contentDescription = null)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}