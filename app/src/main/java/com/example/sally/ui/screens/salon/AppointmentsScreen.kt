package com.example.sally.ui.screens.salon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sally.ui.components.AppointmentCard
import com.example.sally.ui.components.TabButton
import com.example.sally.ui.theme.MainGradient

@Composable
fun AppointmentsScreen(
    navController: NavController,
    initialTab: Int = 0,
    // Inyectamos el ViewModel
    viewModel: AppointmentsViewModel = viewModel()
) {
    // Estado de carga inicial
    LaunchedEffect(Unit) {
        viewModel.fetchAppointments()
    }

    val appointmentsList by viewModel.appointments.collectAsState()
    var selectedTab by remember { mutableStateOf(initialTab) }
    val currentTime = System.currentTimeMillis()

    // Filtros locales (Supabase trae todo, nosotros filtramos en UI por ahora)
    val activeAppointments = appointmentsList.filter {
        it.status == "Active" && it.date >= (currentTime - 86400000)
    }
    val historyAppointments = appointmentsList.filter {
        it.status == "Cancelled" || (it.status == "Active" && it.date < (currentTime - 86400000))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // --- HEADER ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MainGradient)
                .padding(top = 40.dp, bottom = 20.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = null, tint = Color.White)
                }
                Text(
                    "Mis Citas",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // --- TABS ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(25.dp))
                .padding(4.dp)
        ) {
            TabButton(
                text = "Activas",
                count = activeAppointments.size,
                isSelected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                modifier = Modifier.weight(1f)
            )
            TabButton(
                text = "Historial",
                count = historyAppointments.size,
                isSelected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                modifier = Modifier.weight(1f)
            )
        }

        // --- LISTA ---
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val listToShow = if (selectedTab == 0) activeAppointments else historyAppointments

            if (listToShow.isEmpty()) {
                item {
                    Text(
                        "No tienes citas en esta sección.",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(listToShow) { appointment ->
                    AppointmentCard(
                        appointment = appointment,
                        isHistory = selectedTab == 1,
                        onCancel = {
                            // Llamamos al ViewModel para cancelar
                            viewModel.cancelAppointment(appointment.id)
                        }
                    )
                }
            }
        }
    }
}