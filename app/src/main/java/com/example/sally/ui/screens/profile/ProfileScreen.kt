package com.example.sally.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sally.data.SupabaseClient
import com.example.sally.ui.components.StatsCard
import com.example.sally.ui.components.ProfileMenuItem
import com.example.sally.ui.screens.salon.AppointmentsViewModel
import com.example.sally.ui.theme.MainGradient
import com.example.sally.ui.theme.PurpleStart
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    navController: NavController,
    appointmentsViewModel: AppointmentsViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        appointmentsViewModel.fetchAppointments()
    }

    val appointments by appointmentsViewModel.appointments.collectAsState()
    val currentTime = System.currentTimeMillis()

    val activeCount = appointments.count {
        it.status == "Active" && it.date >= (currentTime - 86400000)
    }
    val historyCount = appointments.count {
        it.status == "Cancelled" || (it.status == "Active" && it.date < (currentTime - 86400000))
    }

    val userEmail = remember {
        SupabaseClient.client.auth.currentUserOrNull()?.email ?: "usuario@email.com"
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
    ) {
        // --- HEADER ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        MainGradient,
                        RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)
                    )
            )

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                // Avatar
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE1BEE7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Person,
                        contentDescription = null,
                        modifier = Modifier.size(50.dp),
                        tint = PurpleStart
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = userEmail.split("@")[0].replaceFirstChar { it.uppercase() },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground // Color adaptable
                )
                Text(
                    text = userEmail,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .offset(y = (-30).dp), // Subimos las tarjetas para que pisen el header un poco (efecto visual)
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tarjeta: Mis Citas (Activas) -> Tab 0
            StatsCard(
                title = "Mis Citas",
                value = activeCount.toString(),
                color = Color(0xFF9810FA),
                modifier = Modifier
                    .weight(1f)
                    .clickable { navController.navigate("appointments/0") }
            )

            // Tarjeta: Historial -> Tab 1
            StatsCard(
                title = "Historial",
                value = historyCount.toString(),
                color = Color(0xFFE60076),
                modifier = Modifier
                    .weight(1f)
                    .clickable { navController.navigate("appointments/1") }
            )
        }

        // --- MENÚ ---
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            ProfileMenuItem(Icons.Outlined.Person, "Información Personal") {
                navController.navigate(
                    "profile_personal_info"
                )
            }
            ProfileMenuItem(
                Icons.Outlined.Security,
                "Seguridad"
            ) { navController.navigate("profile_security") }
            ProfileMenuItem(
                Icons.Outlined.Brightness4,
                "Tema"
            ) { navController.navigate("profile_theme") }
            ProfileMenuItem(
                Icons.Outlined.Visibility,
                "Privacidad"
            ) { navController.navigate("profile_privacy") }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Soporte",
                modifier = Modifier.padding(start = 12.dp, bottom = 8.dp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            ProfileMenuItem(
                Icons.Outlined.Help,
                "Centro de Ayuda"
            ) { navController.navigate("profile_help") }
            ProfileMenuItem(
                Icons.Outlined.SupportAgent,
                "Reportar un problema"
            ) { navController.navigate("profile_support") }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Logout
            Button(
                onClick = {
                    scope.launch {
                        SupabaseClient.client.auth.signOut()
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cerrar Sesión", color = Color.Red, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}