package com.mert.locationtracker.data.local.mapper

import com.mert.locationtracker.data.local.entity.LocationEntity
import com.mert.locationtracker.domain.model.Location

fun LocationEntity.toDomain() = Location(
    id = id,
    latitude = latitude,
    longitude = longitude,
    address = address,
    timestamp = timestamp
)


fun Location.toEntity() = LocationEntity(
    id = id,
    latitude = latitude,
    longitude = longitude,
    address = address,
    timestamp = timestamp
)