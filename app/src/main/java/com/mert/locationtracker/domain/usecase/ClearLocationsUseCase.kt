package com.mert.locationtracker.domain.usecase

import com.mert.locationtracker.domain.repository.LocationRepository
import javax.inject.Inject

class ClearLocationsUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    suspend operator fun invoke() {
        repository.clearLocations()
    }
}