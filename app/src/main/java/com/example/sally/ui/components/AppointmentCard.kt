package com.example.sally.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sally.data.local.Appointment
import com.example.sally.ui.components.AppointmentInfoRow
import com.example.sally.ui.theme.PinkEnd
import com.example.sally.ui.theme.PurpleStart
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun AppointmentCard(
    appointment: Appointment,
    isHistory: Boolean,
    onCancel: () -> Unit
) {
    val dateFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
    val dateStr = dateFormat.format(Date(appointment.date))

    val statusLabel = if (appointment.status == "Cancelled") "Cancelada" else "Completada"
    val statusColor = if (appointment.status == "Cancelled") Color(0xFFFF5252) else Color(0xFF4CAF50)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.horizontalGradient(listOf(PurpleStart, PinkEnd)))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(appointment.salonName, color = Color.White, fontWeight = FontWeight.Bold)

                if (isHistory) {
                    Box(
                        modifier = Modifier
                            .background(statusColor, RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(statusLabel, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFD700), RoundedCornerShape(4.dp)) // Amarillo para Próxima
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("Próxima", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                AppointmentInfoRow(Icons.Default.Event, appointment.serviceName, appointment.price)
                Spacer(modifier = Modifier.height(8.dp))
                AppointmentInfoRow(Icons.Default.Person, "Especialista", appointment.specialistName)
                Spacer(modifier = Modifier.height(8.dp))
                AppointmentInfoRow(Icons.Default.Schedule, "Fecha y hora", "$dateStr a las ${appointment.time}")
                Spacer(modifier = Modifier.height(8.dp))
                AppointmentInfoRow(Icons.Default.LocationOn, "Ubicación", appointment.salonAddress)
            }

            Row(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (!isHistory) {
                    Button(
                        onClick = onCancel,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252)), // Rojo
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Cancelar")
                    }
                }

                Button(
                    onClick = { /* TODO: Navegar a detalles completos */ },
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleStart),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isHistory) "Detalles" else "Ver Detalles")
                }
            }
        }
    }
}