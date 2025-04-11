package com.mert.locationtracker.domain.usecase

import com.mert.locationtracker.domain.repository.LocationRepository
import javax.inject.Inject

class GetAddressFromLocationUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    suspend operator fun invoke(latitude: Double, longitude: Double): String? {
        return repository.getAddressFromLocation(latitude, longitude)
    }
}