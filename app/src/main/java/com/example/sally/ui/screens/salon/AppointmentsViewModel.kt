package com.example.sally.ui.screens.salon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sally.data.models.Appointment
import com.example.sally.data.repository.AppointmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AppointmentUiState {
    object Idle : AppointmentUiState()
    object Loading : AppointmentUiState()
    object Success : AppointmentUiState() // Para operaciones como crear/cancelar
    data class Error(val message: String) : AppointmentUiState()
}

class AppointmentsViewModel : ViewModel() {

    private val repository = AppointmentRepository()

    // Lista de citas (para la pantalla "Mis Citas")
    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments: StateFlow<List<Appointment>> = _appointments.asStateFlow()

    // Estado general de la UI (para cargas y errores)
    private val _uiState = MutableStateFlow<AppointmentUiState>(AppointmentUiState.Idle)
    val uiState: StateFlow<AppointmentUiState> = _uiState.asStateFlow()

    // Cargar citas al iniciar (opcional, o llamar manualmente)
    fun fetchAppointments() {
        viewModelScope.launch {
            _uiState.value = AppointmentUiState.Loading
            val result = repository.getUserAppointments()
            _appointments.value = result
            _uiState.value = AppointmentUiState.Idle
        }
    }

    // Crear nueva cita
    fun createAppointment(appointment: Appointment) {
        viewModelScope.launch {
            _uiState.value = AppointmentUiState.Loading
            val result = repository.createAppointment(appointment)

            result.onSuccess {
                _uiState.value = AppointmentUiState.Success
            }.onFailure { error ->
                _uiState.value = AppointmentUiState.Error(error.message ?: "Error desconocido")
            }
        }
    }

    // Cancelar cita
    fun cancelAppointment(appointmentId: Long) {
        viewModelScope.launch {
            // No ponemos Loading global para no bloquear toda la pantalla, o podríamos manejarlo localmente
            repository.cancelAppointment(appointmentId)
            fetchAppointments() // Recargar para ver el cambio de estado
        }
    }

    fun resetState() {
        _uiState.value = AppointmentUiState.Idle
    }
}