package com.example.sally.data.repository

import android.util.Log
import com.example.sally.data.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow

class AuthRepository {

    // Obtener el estado de la sesión (si está logueado o no) en tiempo real
    val sessionStatus: Flow<SessionStatus> = SupabaseClient.client.auth.sessionStatus

    // Iniciar Sesión
    suspend fun login(emailInput: String, passwordInput: String): Result<Unit> {
        return try {
            SupabaseClient.client.auth.signInWith(Email) {
                email = emailInput
                password = passwordInput
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Login error", e)
            Result.failure(e)
        }
    }

    // Registrarse
    suspend fun signUp(emailInput: String, passwordInput: String): Result<Unit> {
        return try {
            SupabaseClient.client.auth.signUpWith(Email) {
                email = emailInput
                password = passwordInput
            }
            // Nota: Dependiendo de tu config en Supabase, puede requerir confirmar email
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("AuthRepository", "SignUp error", e)
            Result.failure(e)
        }
    }

    // Cerrar Sesión
    suspend fun logout() {
        try {
            SupabaseClient.client.auth.signOut()
        } catch (e: Exception) {
            Log.e("AuthRepository", "Logout error", e)
        }
    }

    // Obtener ID del usuario actual (útil para guardar citas)
    fun getCurrentUserId(): String? {
        return SupabaseClient.client.auth.currentUserOrNull()?.id
    }

    // Obtener Email del usuario actual
    fun getCurrentUserEmail(): String? {
        return SupabaseClient.client.auth.currentUserOrNull()?.email
    }
}