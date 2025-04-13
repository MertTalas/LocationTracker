package com.mert.locationtracker.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.mert.locationtracker.R
import com.mert.locationtracker.presentation.viewmodel.LocationViewModel
import com.mert.locationtracker.ui.theme.DarkGray
import com.mert.locationtracker.ui.theme.TrackingGreen
import com.mert.locationtracker.ui.theme.White
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MapScreen(
    viewModel: LocationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val mapProperties = remember { MapProperties(isMyLocationEnabled = true) }
    val mapUiSettings = remember { MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            state.currentUserLocation
                ?: state.locations.lastOrNull()?.let { LatLng(it.latitude, it.longitude) }
                ?: LatLng(41.0082, 28.9784),
            15f
        )
    }

    LaunchedEffect(state.currentUserLocation) {
        state.currentUserLocation?.let {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(it, 15f),
                durationMs = 1000
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = mapProperties,
            uiSettings = mapUiSettings,
            cameraPositionState = cameraPositionState,
            onMapClick = { viewModel.clearSelectedLocation() }
        ) {
            state.locations.forEach { location ->
                val position = LatLng(location.latitude, location.longitude)
                val markerState = rememberMarkerState(position = position)
                val isSelected = state.selectedLocation?.id == location.id

                Marker(
                    state = rememberMarkerState(position = position),
                    title = location.address ?: stringResource(R.string.marker_title),
                    snippet = formatDate(location.timestamp),
                    onClick = {
                        viewModel.selectLocation(location)
                        viewModel.getAddressForSelectedLocation()
                        true
                    }
                )

                if (isSelected) {
                    LaunchedEffect(location) {
                        markerState.showInfoWindow()
                    }

                    MarkerInfoWindow(
                        state = markerState,
                        onInfoWindowClick = {
                            viewModel.clearSelectedLocation()
                        }
                    ) {
                        CustomInfoWindow(
                            address = state.selectedLocation?.address ?: "${location.latitude}, ${location.longitude}"
                        )
                    }
                }
            }
        }

        state.currentUserLocation?.let { location ->
            MyLocationButton(
                onClick = {
                    coroutineScope.launch {
                        cameraPositionState.animate(
                            update = CameraUpdateFactory.newLatLngZoom(location, 15f),
                            durationMs = 1000
                        )
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 32.dp, end = 16.dp)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TrackingButton(
                text = if (state.isTracking) stringResource(R.string.tracking_btn_stop)
                else stringResource(R.string.tracking_btn_follow),
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
                Text(stringResource(R.string.reset_route_btn))
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
fun CustomInfoWindow(address: String) {
    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
            .widthIn(min = 200.dp, max = 300.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = address,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = Center,
                maxLines = 3,
                overflow = Ellipsis
            )
        }
    }
}

@Composable
fun MyLocationButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
            .size(32.dp),
        shape = RoundedCornerShape(8.dp),
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = stringResource(R.string.point_my_location_icon_cd)
        )
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