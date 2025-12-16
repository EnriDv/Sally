package com.example.sally.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sally.data.local.AppDatabase
import com.example.sally.data.local.Appointment
import com.example.sally.ui.components.AppointmentCard
import com.example.sally.ui.components.TabButton
import com.example.sally.ui.theme.BackgroundColor
import com.example.sally.ui.theme.MainGradient
import com.example.sally.ui.theme.PinkEnd
import com.example.sally.ui.theme.PurpleStart
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AppointmentsScreen(
    navController: NavController,
    initialTab: Int = 0
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dao = remember { AppDatabase.getDatabase(context).appointmentDao() }

    val appointmentsList by dao.getAllAppointments().collectAsState(initial = emptyList())

    var selectedTab by remember { mutableStateOf(initialTab) }

    val currentTime = System.currentTimeMillis()

    val activeAppointments = appointmentsList.filter {
        it.status == "Active" && it.date >= (currentTime - 86400000)
    }

    val historyAppointments = appointmentsList.filter {
        it.status == "Cancelled" || (it.status == "Active" && it.date < (currentTime - 86400000))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White, RoundedCornerShape(25.dp))
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

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val listToShow = if (selectedTab == 0) activeAppointments else historyAppointments

            items(listToShow) { appointment ->
                AppointmentCard(
                    appointment = appointment,
                    isHistory = selectedTab == 1,
                    onCancel = {
                        scope.launch { dao.cancelAppointment(appointment.id) }
                    }
                )
            }
        }
    }
}

