package com.mert.locationtracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mert.locationtracker.domain.model.Location
import com.mert.locationtracker.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val getSavedLocationsUseCase: GetSavedLocationsUseCase,
    private val startLocationTrackingUseCase: StartLocationTrackingUseCase,
    private val stopLocationTrackingUseCase: StopLocationTrackingUseCase,
    private val clearLocationsUseCase: ClearLocationsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LocationUiState())
    val state: StateFlow<LocationUiState> = _state

    init {
        getLocations()
    }

    private fun getLocations() {
        viewModelScope.launch {
            getSavedLocationsUseCase().collect { locations ->
                _state.update { it.copy(
                    locations = locations,
                    isLoading = false
                ) }
            }
        }
    }

    fun startLocationTracking() {
        startLocationTrackingUseCase()
        _state.update { it.copy(isTracking = true) }
    }

    fun stopLocationTracking() {
        stopLocationTrackingUseCase()
        _state.update { it.copy(isTracking = false) }
    }

    fun clearLocations() {
        viewModelScope.launch {
            clearLocationsUseCase()
        }
    }

    fun selectLocation(location: Location?) {
        _state.update { it.copy(selectedLocation = location) }
    }
}

data class LocationUiState(
    val locations: List<Location> = emptyList(),
    val isTracking: Boolean = false,
    val isLoading: Boolean = true,
    val selectedLocation: Location? = null,
    val error: String? = null
) {
    val hasLocations: Boolean get() = locations.isNotEmpty()
}