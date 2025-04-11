package com.mert.locationtracker.domain.model

import java.util.Date

data class Location(
    val id: Long = 0,
    val latitude: Double,
    val longitude: Double,
    val address: String?,
    val timestamp: Date
)