package com.example.sally.data.repository

import android.util.Log
import com.example.sally.data.models.Specialist
import com.example.sally.data.SupabaseClient
import io.github.jan.supabase.postgrest.from

class SpecialistRepository {

    suspend fun getSpecialistsBySalon(salonId: Long): List<Specialist> {
        return try {
            SupabaseClient.client
                .from("specialists")
                .select {
                    filter {
                        eq("salon_id", salonId)
                    }
                }
                .decodeList<Specialist>()
        } catch (e: Exception) {
            Log.e("SpecialistRepo", "Error fetching specialists", e)
            emptyList()
        }
    }
}