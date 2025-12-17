package com.example.sally.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sally.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Estados posibles de la pantalla de Auth
sealed class AuthUiState {
    object Idle : AuthUiState()      // Esperando acción
    object Loading : AuthUiState()   // Cargando (Spinner)
    object Success : AuthUiState()   // Login/Registro exitoso
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    // Estado observable por la UI
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _uiState.value = AuthUiState.Error("Por favor completa todos los campos")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = repository.login(email, pass)

            result.onSuccess {
                _uiState.value = AuthUiState.Success
            }.onFailure { error ->
                // Mensaje amigable si falla
                val msg = if (error.message?.contains("Invalid login") == true)
                    "Correo o contraseña incorrectos"
                else
                    "Error: ${error.message}"
                _uiState.value = AuthUiState.Error(msg)
            }
        }
    }

    fun signUp(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _uiState.value = AuthUiState.Error("Por favor completa todos los campos")
            return
        }

        if (pass.length < 6) {
            _uiState.value = AuthUiState.Error("La contraseña debe tener al menos 6 caracteres")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = repository.signUp(email, pass)

            result.onSuccess {
                _uiState.value = AuthUiState.Success
            }.onFailure { error ->
                _uiState.value = AuthUiState.Error("Error al registrarse: ${error.message}")
            }
        }
    }

    // Función para resetear el estado (ej: al cambiar entre login y registro)
    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}