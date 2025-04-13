package com.mert.locationtracker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.mert.locationtracker.presentation.ui.MapScreen
import com.mert.locationtracker.presentation.ui.SplashScreen
import com.mert.locationtracker.ui.theme.LocationTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var showSplashScreen by mutableStateOf(true)
    private var isRequestingPermissions by mutableStateOf(false)

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        isRequestingPermissions = false
        val foregroundLocationGranted =
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (foregroundLocationGranted) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (!hasPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                    requestBackgroundLocationPermission()
                } else {
                    showSplashScreen = false
                }
            } else {
                showSplashScreen = false
            }
        } else {
            Toast.makeText(
                this,
                "Konum izleme için konum izinleri gereklidir",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private val backgroundPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        isRequestingPermissions = false
        if (!isGranted) {
            Toast.makeText(
                this,
                "Arka planda konum izleme için izin gereklidir",
                Toast.LENGTH_LONG
            ).show()
        }

        showSplashScreen = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestLocationPermissions()

        setContent {
            LocationTrackerTheme(transparentStatusBar = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (showSplashScreen) {
                        SplashScreen(
                            onPermissionsGranted = {
                                showSplashScreen = false
                            },
                            onRequestPermissions = {
                                redirectSettingsMenu()
                            },
                            hasRequiredPermissions = hasRequiredPermissions(),
                            isRequestingPermissions = isRequestingPermissions
                        )
                    } else {
                        MapScreen()
                    }
                }
            }
        }
    }

    private fun requestLocationPermissions() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        isRequestingPermissions = true
        locationPermissionLauncher.launch(permissions)
    }

    private fun redirectSettingsMenu() {
        openAppSettings()
        Toast.makeText(
            this,
            "Lütfen ayarlardan konum izinlerini etkinleştirin",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    private fun requestBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            isRequestingPermissions = true
            backgroundPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
    }

    private fun hasPermission(permission: String): Boolean =
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

    private fun hasMinimumLocationPermissions(): Boolean =
        hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) ||
                hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)

    private fun hasRequiredPermissions(): Boolean {
        val hasForegroundLocation = hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) ||
                hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            hasForegroundLocation && hasPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        } else {
            hasForegroundLocation
        }
    }

    override fun onResume() {
        super.onResume()
        if (hasMinimumLocationPermissions()) {
            showSplashScreen = false
        }
    }
}