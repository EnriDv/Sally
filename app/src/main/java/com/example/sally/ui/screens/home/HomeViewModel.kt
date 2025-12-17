package com.example.sally.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sally.data.models.Salon
import com.example.sally.data.models.Service // Importar Service
import com.example.sally.data.repository.SalonRepository
import com.example.sally.data.repository.ServiceRepository // Importar Repo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val salonRepository = SalonRepository()
    private val serviceRepository = ServiceRepository() // Nuevo Repo

    // Estado Salones
    private val _salons = MutableStateFlow<List<Salon>>(emptyList())
    val salons: StateFlow<List<Salon>> = _salons.asStateFlow()

    // Estado Servicios Generales (Nuevo)
    private val _services = MutableStateFlow<List<Service>>(emptyList())
    val services: StateFlow<List<Service>> = _services.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchHomeData()
    }

    private fun fetchHomeData() {
        viewModelScope.launch {
            _isLoading.value = true

            // Cargamos ambas cosas
            val salonsResult = salonRepository.getSalons()
            val servicesResult = serviceRepository.getGeneralServices()

            _salons.value = salonsResult
            _services.value = servicesResult

            _isLoading.value = false
        }
    }
}