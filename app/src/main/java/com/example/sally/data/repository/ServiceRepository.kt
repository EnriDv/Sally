package com.example.sally.data.repository

import android.util.Log
import com.example.sally.data.SupabaseClient
import com.example.sally.data.models.Service
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.filter.FilterOperator

class ServiceRepository {

    suspend fun getServicesBySalon(salonId: Long): List<Service> {
        return try {
            SupabaseClient.client
                .from("services")
                .select {
                    filter {
                        eq("salon_id", salonId)
                    }
                }
                .decodeList<Service>()
        } catch (e: Exception) {
            Log.e("ServiceRepo", "Error fetching services", e)
            emptyList()
        }
    }

    suspend fun getGeneralServices(): List<Service> {
        return try {
            SupabaseClient.client
                .from("services")
                .select {
                    filter {
                        filter("salon_id", FilterOperator.IS, "null")
                    }
                }
                .decodeList<Service>()
        } catch (e: Exception) {
            Log.e("ServiceRepo", "Error fetching general services", e)
            emptyList()
        }
    }
}