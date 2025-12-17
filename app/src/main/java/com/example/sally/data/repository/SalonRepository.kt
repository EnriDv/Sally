package com.example.sally.data.repository

import android.util.Log
import com.example.sally.data.models.Salon
import com.example.sally.data.SupabaseClient
import io.github.jan.supabase.postgrest.from

class SalonRepository {

    suspend fun getSalons(): List<Salon> {
        return try {
            val salons = SupabaseClient.client
                .from("salons")
                .select()
                .decodeList<Salon>()

            salons
        } catch (e: Exception) {
            Log.e("SalonRepository", "Error fetching salons", e)
            emptyList()
        }
    }
}