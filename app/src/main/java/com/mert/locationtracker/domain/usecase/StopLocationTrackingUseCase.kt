package com.mert.locationtracker.domain.usecase

import android.content.Context
import android.content.Intent
import com.mert.locationtracker.data.service.LocationService
import javax.inject.Inject

class StopLocationTrackingUseCase @Inject constructor(
    private val context: Context
) {
    operator fun invoke() {
        val intent = Intent(context, LocationService::class.java).apply {
            action = LocationService.ACTION_STOP
        }
        context.startService(intent)
    }
}