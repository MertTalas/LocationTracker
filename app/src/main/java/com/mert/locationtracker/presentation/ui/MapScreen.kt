package com.mert.locationtracker.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.mert.locationtracker.presentation.viewmodel.LocationViewModel
import com.mert.locationtracker.ui.theme.DarkGray
import com.mert.locationtracker.ui.theme.TrackingGreen
import com.mert.locationtracker.ui.theme.White
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MapScreen(
    viewModel: LocationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val mapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }
    val mapUiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = false)) }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = mapProperties,
            uiSettings = mapUiSettings,
            cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(
                    state.locations.lastOrNull()?.let { LatLng(it.latitude, it.longitude) }
                        ?: LatLng(41.0082, 28.9784), // Default: İstanbul
                    15f
                )
            }
        ) {
            state.locations.forEach { location ->
                Marker(
                    state = rememberMarkerState(position = LatLng(location.latitude, location.longitude)),
                    title = location.address ?: "Konum",
                    snippet = formatDate(location.timestamp),
                    onClick = {
                        viewModel.selectLocation(location)
                        true
                    }
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TrackingButton(
                text = if (state.isTracking) "Konumu Takip Etmeyi Durdur" else "Konumu Takip Et",
                onClick = {
                    if (state.isTracking) {
                        viewModel.stopLocationTracking()
                    } else {
                        viewModel.startLocationTracking()
                    }
                },
                isTracking = state.isTracking,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { viewModel.clearLocations() },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.hasLocations
            ) {
                Text("Rotayı Sıfırla")
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun TrackingButton(
    text: String,
    onClick: () -> Unit,
    isTracking: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isTracking) TrackingGreen else White,
            contentColor = if (isTracking) White else DarkGray
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Medium
            )
        )
    }
}

private fun formatDate(date: Date): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return dateFormat.format(date)
}