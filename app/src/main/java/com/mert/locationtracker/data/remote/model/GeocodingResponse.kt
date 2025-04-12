package com.mert.locationtracker.data.remote.model

import com.google.gson.annotations.SerializedName

data class GeocodingResponse(
    @SerializedName("error_message") val errorMessage: String?,
    @SerializedName("results") val results: List<GeocodingResult>,
    @SerializedName("status") val status: String
)

data class GeocodingResult(
    @SerializedName("formatted_address") val formattedAddress: String
)