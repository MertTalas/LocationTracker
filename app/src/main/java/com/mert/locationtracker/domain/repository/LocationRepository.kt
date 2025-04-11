package com.mert.locationtracker.domain.repository

import com.mert.locationtracker.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getLocations(): Flow<List<Location>>
    suspend fun addLocation(location: Location)
    suspend fun clearLocations()
    suspend fun getAddressFromLocation(latitude: Double, longitude: Double): String?
}