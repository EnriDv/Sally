package com.example.sally.ui.screens.profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.sally.data.local.AppDatabase
import com.example.sally.ui.components.MenuItem
import com.example.sally.ui.components.MenuSection
import com.example.sally.ui.components.StatsCard
import com.example.sally.ui.theme.MainGradient
import com.example.sally.ui.theme.PurpleStart

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val dao = remember { AppDatabase.getDatabase(context).appointmentDao() }

    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> hasNotificationPermission = isGranted }
    )

    val allAppointments by dao.getAllAppointments().collectAsState(initial = emptyList())
    val currentTime = System.currentTimeMillis()
    val activeCount =
        allAppointments.count { it.status == "Active" && it.date >= (currentTime - 86400000) }
    val historyCount =
        allAppointments.count { it.status == "Cancelled" || (it.status == "Active" && it.date < (currentTime - 86400000)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 100.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .padding(bottom = 24.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(MainGradient)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .border(3.dp, Color.White, CircleShape)
                                .background(Color.Gray)
                        )
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.BottomEnd)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = null,
                                tint = PurpleStart,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Jane Doe",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "jhon.doe@email.com",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { navController.navigate("profile_personal_info") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
                    ) {
                        Text("Editar Perfil", color = Color.White)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatsCard(
                    title = "Mis Citas",
                    value = activeCount.toString(),
                    color = Color(0xFF9810FA),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { navController.navigate("appointments/0") }
                )
                StatsCard(
                    title = "Historial",
                    value = historyCount.toString(),
                    color = Color(0xFFE60076),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { navController.navigate("appointments/1") }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        MenuSection(title = "Configuración de Cuenta") {
            MenuItem(
                icon = Icons.Outlined.Person,
                text = "Información Personal",
                subtitle = "Actualiza tus datos",
                onClick = { navController.navigate("profile_personal_info") }
            )
            HorizontalDivider(
                modifier = Modifier.padding(start = 56.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )
            MenuItem(
                icon = Icons.Outlined.Email,
                text = "Seguridad",
                subtitle = "Email y Contraseña",
                onClick = { navController.navigate("profile_security") }
            )
        }

        MenuSection(title = "Preferencias") {
            MenuItem(
                icon = Icons.Outlined.Notifications,
                text = "Notificaciones",
                subtitle = if (hasNotificationPermission) "Activadas" else "Desactivadas",
                hasSwitch = true,
                isSwitchChecked = hasNotificationPermission,
                onSwitchChanged = {
                    if (!hasNotificationPermission) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    } else {
                        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        }
                        context.startActivity(intent)
                    }
                }
            )
            HorizontalDivider(
                modifier = Modifier.padding(start = 56.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )
            MenuItem(
                icon = Icons.Outlined.Language,
                text = "Apariencia",
                subtitle = "Cambia de Tema",
                onClick = { navController.navigate("profile_theme") }
            )
            HorizontalDivider(
                modifier = Modifier.padding(start = 56.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )
            MenuItem(
                icon = Icons.Outlined.Lock,
                text = "Privacidad",
                subtitle = "Controla tu información",
                onClick = { navController.navigate("profile_privacy") }
            )
        }

        MenuSection(title = "Ayuda y Soporte") {
            MenuItem(
                icon = Icons.AutoMirrored.Outlined.Help,
                text = "Centro de Ayuda",
                subtitle = "FAQ y tutoriales",
                onClick = { navController.navigate("profile_help") }
            )
            HorizontalDivider(
                modifier = Modifier.padding(start = 56.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )
            MenuItem(
                icon = Icons.Outlined.Mail,
                text = "Contactar Soporte",
                subtitle = "Estamos aquí para ayudarte",
                onClick = { navController.navigate("profile_support") }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* TODO: Logout Logic */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface), // CORREGIDO: Adaptable
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Cerrar Sesión",
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}