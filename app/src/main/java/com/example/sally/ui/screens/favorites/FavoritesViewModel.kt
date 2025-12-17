package com.example.sally.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sally.data.models.Salon
import com.example.sally.data.repository.FavoritesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel() {

    private val repo = FavoritesRepository()

    private val _favoriteSalons = MutableStateFlow<List<Salon>>(emptyList())
    val favoriteSalons: StateFlow<List<Salon>> = _favoriteSalons.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadFavorites() {
        viewModelScope.launch {
            _isLoading.value = true
            _favoriteSalons.value = repo.getUserFavorites()
            _isLoading.value = false
        }
    }
}