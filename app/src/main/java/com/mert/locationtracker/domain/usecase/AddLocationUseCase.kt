package com.mert.locationtracker.domain.usecase

import com.mert.locationtracker.domain.model.Location
import com.mert.locationtracker.domain.repository.LocationRepository
import javax.inject.Inject

class AddLocationUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    suspend operator fun invoke(location: Location) {
        repository.addLocation(location)
    }
}