package com.mert.locationtracker.presentation.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.mert.locationtracker.domain.model.Location
import com.mert.locationtracker.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    private val clearLocationsUseCase: ClearLocationsUseCase,
    private val getAddressFromLocationUseCase: GetAddressFromLocationUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(LocationUiState())
    val state: StateFlow<LocationUiState> = _state

    private val fusedLocationClient by lazy { LocationServices.getFusedLocationProviderClient(context) }

    init {
        getLocations()
        getCurrentLocation()
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

    private fun getCurrentLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                viewModelScope.launch {
                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        location?.let {
                            _state.update { state ->
                                state.copy(
                                    currentUserLocation = LatLng(
                                        location.latitude,
                                        location.longitude
                                    )
                                )
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
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

    fun getAddressForSelectedLocation() {
        viewModelScope.launch {
            val selectedLocation = _state.value.selectedLocation ?: return@launch

            if (selectedLocation.address.isNullOrEmpty()) {
                val address = getAddressFromLocationUseCase(
                    selectedLocation.latitude,
                    selectedLocation.longitude
                )

                _state.update { it.copy(
                    selectedLocation = selectedLocation.copy(address = address)
                ) }
            }
        }
    }

    fun clearSelectedLocation() {
        _state.update { it.copy(selectedLocation = null) }
    }
}

data class LocationUiState(
    val locations: List<Location> = emptyList(),
    val isTracking: Boolean = false,
    val isLoading: Boolean = true,
    val selectedLocation: Location? = null,
    val currentUserLocation: LatLng? = null
) {
    val hasLocations: Boolean get() = locations.isNotEmpty()
}