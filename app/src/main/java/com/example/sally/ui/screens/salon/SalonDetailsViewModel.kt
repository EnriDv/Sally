package com.example.sally.ui.screens.salon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sally.data.models.Service
import com.example.sally.data.models.Specialist
import com.example.sally.data.repository.FavoritesRepository // Importante
import com.example.sally.data.repository.ServiceRepository
import com.example.sally.data.repository.SpecialistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SalonDetailsViewModel : ViewModel() {

    private val serviceRepo = ServiceRepository()
    private val specialistRepo = SpecialistRepository()
    private val favoritesRepo = FavoritesRepository() // Repo de Favoritos

    private val _services = MutableStateFlow<List<Service>>(emptyList())
    val services: StateFlow<List<Service>> = _services.asStateFlow()

    private val _specialists = MutableStateFlow<List<Specialist>>(emptyList())
    val specialists: StateFlow<List<Specialist>> = _specialists.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // ESTADO: ¿Es favorito este salón?
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    fun loadSalonDetails(salonId: Long) {
        viewModelScope.launch {
            _isLoading.value = true

            val servicesResult = serviceRepo.getServicesBySalon(salonId)
            val specialistsResult = specialistRepo.getSpecialistsBySalon(salonId)

            // Verificamos si ya le dimos like
            val isFav = favoritesRepo.isFavorite(salonId)

            _services.value = servicesResult
            _specialists.value = specialistsResult
            _isFavorite.value = isFav // Actualizamos el estado

            _isLoading.value = false
        }
    }

    // FUNCIÓN PARA EL BOTÓN DE CORAZÓN
    fun toggleFavorite(salonId: Long) {
        viewModelScope.launch {
            val current = _isFavorite.value
            // Optimismo UI: cambiamos el color inmediatamente
            _isFavorite.value = !current

            if (current) {
                favoritesRepo.removeFavorite(salonId) // Si era fav, lo quitamos
            } else {
                favoritesRepo.addFavorite(salonId) // Si no era, lo agregamos
            }
        }
    }
}