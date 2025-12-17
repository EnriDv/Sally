package com.example.sally.ui.screens.salon

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sally.NotificationUtils
import com.example.sally.data.models.Appointment
import com.example.sally.data.models.Specialist
// import com.example.sally.data.models.mockSpecialists <--- BORRADO (Ya no lo usamos)
import com.example.sally.ui.components.SpecialistSelectionItem
import com.example.sally.ui.components.TimeSlotChip
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    navController: NavController,
    salonId: Long, // <--- NUEVO PARÁMETRO
    salonName: String,
    salonAddress: String,
    serviceName: String,
    servicePrice: String,
    viewModel: AppointmentsViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    // OBSERVAMOS LA LISTA REAL DE ESPECIALISTAS
    val specialists by viewModel.specialists.collectAsState()

    // CARGAMOS LOS ESPECIALISTAS AL ENTRAR
    LaunchedEffect(salonId) {
        viewModel.loadSpecialists(salonId)
    }

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
    LaunchedEffect(Unit) {
        NotificationUtils.createNotificationChannel(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is AppointmentUiState.Success) {
            Toast.makeText(context, "¡Cita agendada con éxito!", Toast.LENGTH_SHORT).show()
            if (hasNotificationPermission) {
                NotificationUtils.showImmediateNotification(context, "Reserva Confirmada", "Tu cita en $salonName ha sido guardada.")
            }
            navController.navigate("home") {
                popUpTo("home") { inclusive = true }
            }
            viewModel.resetState()
        } else if (uiState is AppointmentUiState.Error) {
            val errorMsg = (uiState as AppointmentUiState.Error).message
            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
            viewModel.resetState()
        }
    }

    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= System.currentTimeMillis() - 86400000
            }
            override fun isSelectableYear(year: Int): Boolean {
                return year >= Calendar.getInstance().get(Calendar.YEAR)
            }
        }
    )
    var selectedTime by remember { mutableStateOf<String?>(null) }
    var selectedSpecialist by remember { mutableStateOf<Specialist?>(null) }

    val decodedAddress = remember(salonAddress) { URLDecoder.decode(salonAddress, StandardCharsets.UTF_8.toString()) }
    val decodedServiceName = remember(serviceName) { URLDecoder.decode(serviceName, StandardCharsets.UTF_8.toString()) }
    val timeSlots = listOf("9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "2:00 PM", "3:00 PM", "4:00 PM")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Agenda una Cita", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Button(
                    onClick = {
                        val dateMillis = datePickerState.selectedDateMillis
                        if (dateMillis != null && selectedTime != null && selectedSpecialist != null) {

                            val newAppointment = Appointment(
                                salonName = salonName,
                                salonAddress = decodedAddress,
                                date = dateMillis,
                                time = selectedTime!!,
                                specialistName = selectedSpecialist!!.name,
                                serviceName = decodedServiceName,
                                price = servicePrice,
                                status = "Active"
                            )

                            viewModel.createAppointment(newAppointment)

                        } else {
                            Toast.makeText(context, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                        }
                    },
                    enabled = uiState !is AppointmentUiState.Loading,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    if (uiState is AppointmentUiState.Loading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Confirmar Cita ($servicePrice)", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                val colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                    todayDateBorderColor = MaterialTheme.colorScheme.primary,
                    containerColor = MaterialTheme.colorScheme.surface
                )
                DatePicker(state = datePickerState, colors = colors, showModeToggle = false, modifier = Modifier.padding(8.dp), title = null, headline = null)
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Horarios Disponibles", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(12.dp))

            val chunkedSlots = timeSlots.chunked(2)
            chunkedSlots.forEach { pair ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    pair.forEach { time ->
                        TimeSlotChip(time = time, isSelected = time == selectedTime, onSelect = { selectedTime = time }, modifier = Modifier.weight(1f))
                    }
                    if (pair.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Seleccionar Especialista", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(12.dp))

            // AQUI ESTABA EL MOCK, AHORA USAMOS 'specialists' QUE VIENE DE SUPABASE
            if (specialists.isEmpty()) {
                Text("Cargando especialistas...", fontSize = 14.sp, color = Color.Gray)
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(horizontal = 4.dp)) {
                    items(specialists) { specialist ->
                        SpecialistSelectionItem(
                            specialist = specialist,
                            isSelected = specialist == selectedSpecialist,
                            onClick = { selectedSpecialist = specialist }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}