package com.example.sally.data.repository

import android.util.Log
import com.example.sally.data.models.Salon
import com.example.sally.data.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Count
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class FavoritesRepository {

    @Serializable
    data class FavoriteRequest(
        @SerialName("user_id") val userId: String,
        @SerialName("salon_id") val salonId: Long
    )

    @Serializable
    data class FavoriteId(@SerialName("salon_id") val salonId: Long)

    // Verificar si ya le di like (para pintar el corazón rojo o blanco)
    suspend fun isFavorite(salonId: Long): Boolean {
        val userId = SupabaseClient.client.auth.currentUserOrNull()?.id ?: return false
        return try {
            val count = SupabaseClient.client.from("favorites").select {
                filter {
                    eq("user_id", userId)
                    eq("salon_id", salonId)
                }
                count(Count.EXACT)
            }.countOrNull() ?: 0
            count > 0
        } catch (e: Exception) {
            Log.e("FavRepo", "Check fav error", e)
            false
        }
    }

    // DAR LIKE
    suspend fun addFavorite(salonId: Long) {
        val userId = SupabaseClient.client.auth.currentUserOrNull()?.id ?: return
        try {
            val item = FavoriteRequest(userId, salonId)
            SupabaseClient.client.from("favorites").insert(item)
        } catch (e: Exception) {
            Log.e("FavRepo", "Add fav error", e)
        }
    }

    // QUITAR LIKE
    suspend fun removeFavorite(salonId: Long) {
        val userId = SupabaseClient.client.auth.currentUserOrNull()?.id ?: return
        try {
            SupabaseClient.client.from("favorites").delete {
                filter {
                    eq("user_id", userId)
                    eq("salon_id", salonId)
                }
            }
        } catch (e: Exception) {
            Log.e("FavRepo", "Remove fav error", e)
        }
    }

    // OBTENER TODOS MIS FAVORITOS (Para la pantalla de Favoritos)
    suspend fun getUserFavorites(): List<Salon> {
        val userId = SupabaseClient.client.auth.currentUserOrNull()?.id ?: return emptyList()
        return try {
            // 1. Obtener IDs
            val favoriteIds = SupabaseClient.client.from("favorites")
                .select(columns = Columns.list("salon_id")) {
                    filter { eq("user_id", userId) }
                }.decodeList<FavoriteId>().map { it.salonId }

            if (favoriteIds.isEmpty()) return emptyList()

            // 2. Obtener Salones completos
            SupabaseClient.client.from("salons").select {
                filter { isIn("id", favoriteIds) }
            }.decodeList<Salon>()

        } catch (e: Exception) {
            Log.e("FavRepo", "Get favs error", e)
            emptyList()
        }
    }
}