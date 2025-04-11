package com.mert.locationtracker.domain.usecase

import com.mert.locationtracker.domain.model.Location
import com.mert.locationtracker.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedLocationsUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    operator fun invoke(): Flow<List<Location>> {
        return repository.getLocations()
    }
}