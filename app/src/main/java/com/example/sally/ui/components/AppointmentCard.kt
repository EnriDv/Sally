package com.example.sally.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sally.data.models.Appointment // <--- IMPORTANTE: Usar el modelo nuevo
import com.example.sally.ui.theme.GrayText
import com.example.sally.ui.theme.PurpleStart
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AppointmentCard(
    appointment: Appointment,
    isHistory: Boolean = false,
    onCancel: () -> Unit = {}
) {
    // Formatear la fecha (de Timestamp Long a Texto legible)
    val dateString = remember(appointment.date) {
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale("es", "ES"))
        formatter.format(Date(appointment.date))
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Encabezado: Nombre del Salón y Precio
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = appointment.salonName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = appointment.price,
                    fontWeight = FontWeight.Bold,
                    color = PurpleStart
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Servicio y Especialista
            Text(
                text = "${appointment.serviceName} con ${appointment.specialistName}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Datos de Fecha, Hora y Dirección
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = GrayText, modifier = Modifier.size(14.dp))
                Text(" $dateString", fontSize = 12.sp, color = GrayText)
                Spacer(modifier = Modifier.width(12.dp))
                Icon(Icons.Default.Schedule, contentDescription = null, tint = GrayText, modifier = Modifier.size(14.dp))
                Text(" ${appointment.time}", fontSize = 12.sp, color = GrayText)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = GrayText, modifier = Modifier.size(14.dp))
                Text(
                    " ${appointment.salonAddress}",
                    fontSize = 12.sp,
                    color = GrayText,
                    maxLines = 1
                )
            }

            // Botón de Cancelar (Solo si no es historial y está activa)
            if (!isHistory && appointment.status == "Active") {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth().height(36.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("Cancelar Cita", fontSize = 12.sp)
                }
            } else if (appointment.status == "Cancelled") {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Cancelado",
                    color = Color.Red,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}