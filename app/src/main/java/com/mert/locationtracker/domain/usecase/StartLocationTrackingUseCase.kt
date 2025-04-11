package com.mert.locationtracker.domain.usecase

import android.content.Context
import android.content.Intent
import com.mert.locationtracker.data.service.LocationService
import javax.inject.Inject

class StartLocationTrackingUseCase @Inject constructor(
    private val context: Context
) {
    operator fun invoke() {
        val intent = Intent(context, LocationService::class.java).apply {
            action = LocationService.ACTION_START
        }
        context.startService(intent)
    }
}