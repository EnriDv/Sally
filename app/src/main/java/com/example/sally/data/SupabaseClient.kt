package com.example.sally.data

import com.example.sally.BuildConfig
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.github.jan.supabase.storage.Storage
import kotlinx.serialization.json.Json
import io.ktor.client.engine.android.Android

object SupabaseClient {

    val client = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_KEY
    ) {
        httpEngine = Android.create()

        defaultSerializer = KotlinXSerializer(Json {
            ignoreUnknownKeys = true

            encodeDefaults = false

            isLenient = true
        })

        install(Auth)
        install(Postgrest)
        install(Storage)
    }
}