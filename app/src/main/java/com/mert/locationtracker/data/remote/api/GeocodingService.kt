package com.mert.locationtracker.data.remote.api

import com.mert.locationtracker.BuildConfig
import com.mert.locationtracker.data.remote.model.GeocodingResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingService {
    @GET("geocode/json")
    suspend fun getAddressFromLocation(
        @Query("latlng") latLng: String,
        @Query("sensor") sensor: String,
        @Query("language") language: String,
        @Query("key") apiKey: String = BuildConfig.MAPS_API_KEY
    ): GeocodingResponse
}