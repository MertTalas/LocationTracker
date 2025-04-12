package com.mert.locationtracker.data.repository

import com.mert.locationtracker.data.local.dao.LocationDao
import com.mert.locationtracker.data.local.mapper.toDomain
import com.mert.locationtracker.data.local.mapper.toEntity
import com.mert.locationtracker.data.remote.api.GeocodingService
import com.mert.locationtracker.domain.model.Location
import com.mert.locationtracker.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationDao: LocationDao,
    private val geocodingService: GeocodingService
) : LocationRepository {

    override fun getLocations(): Flow<List<Location>> {
        return locationDao.getAllLocations().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addLocation(location: Location) {
        locationDao.insertLocation(location.toEntity())
    }

    override suspend fun clearLocations() {
        locationDao.clearAllLocations()
    }

    override suspend fun getAddressFromLocation(latitude: Double, longitude: Double): String? {
        return try {
            val response = geocodingService.getAddressFromLocation(
                "$latitude,$longitude",
                "true",
                "tr"
            )
            response.results.firstOrNull()?.formattedAddress
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}