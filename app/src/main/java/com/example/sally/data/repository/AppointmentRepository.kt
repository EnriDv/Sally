package com.example.sally.data.repository

import android.util.Log
import com.example.sally.data.models.Appointment
import com.example.sally.data.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from

class AppointmentRepository {

    // Crear una nueva cita
    suspend fun createAppointment(appointment: Appointment): Result<Unit> {
        return try {
            // Obtenemos el usuario actual para asignarlo a la cita
            val currentUser = SupabaseClient.client.auth.currentUserOrNull()

            if (currentUser == null) {
                return Result.failure(Exception("Usuario no autenticado"))
            }

            // Creamos una copia del objeto asignándole el user_id real
            val appointmentWithUser = appointment.copy(userId = currentUser.id)

            SupabaseClient.client.from("appointments").insert(appointmentWithUser)

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("AppointmentRepo", "Error creating appointment", e)
            Result.failure(e)
        }
    }

    // Obtener citas del usuario (Supabase ya filtra por user_id gracias a las Policies RLS)
    suspend fun getUserAppointments(): List<Appointment> {
        return try {
            val result = SupabaseClient.client
                .from("appointments")
                .select() // Las policies de Supabase se encargan de filtrar solo las mías
                .decodeList<Appointment>()

            // Ordenamos por fecha (más reciente primero)
            result.sortedByDescending { it.date }
        } catch (e: Exception) {
            Log.e("AppointmentRepo", "Error fetching appointments", e)
            emptyList()
        }
    }

    // Cancelar una cita (Actualizar estado)
    suspend fun cancelAppointment(appointmentId: Long): Result<Unit> {
        return try {
            SupabaseClient.client.from("appointments")
                .update({
                    set("status", "Cancelled")
                }) {
                    filter {
                        eq("id", appointmentId)
                    }
                }
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("AppointmentRepo", "Error cancelling appointment", e)
            Result.failure(e)
        }
    }
}