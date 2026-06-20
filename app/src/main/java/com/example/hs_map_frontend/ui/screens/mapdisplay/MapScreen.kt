package com.example.hs_map_frontend.ui.screens.mapdisplay

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.example.hs_map_frontend.ui.screens.auth.AppLanguage
import com.example.hs_map_frontend.ui.viewmodels.MainViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.location.LocationComponentActivationOptions
import org.maplibre.android.location.modes.CameraMode
import org.maplibre.android.location.modes.RenderMode
import org.maplibre.android.maps.MapLibreMap as LibMapInstance
import org.maplibre.android.maps.MapView

enum class MicState { IDLE, LISTENING }

private const val MAP_STYLE_URL =
    "https://tiles.stadiamaps.com/styles/alidade_smooth.json?api_key=0d6c1b5e-cb7f-4a4e-af7f-f162f9cefa53"

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapDisplayScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    var micState by remember { mutableStateOf(MicState.IDLE) }
    var sidebarOpen by remember { mutableStateOf(false) }
    var mapInstance by remember { mutableStateOf<LibMapInstance?>(null) }
    val isUrdu = mainViewModel.language == AppLanguage.URDU

    val locationPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    LaunchedEffect(Unit) {
        if (!locationPermissionState.allPermissionsGranted) {
            locationPermissionState.launchMultiplePermissionRequest()
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "mic_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.5f,
        animationSpec = infiniteRepeatable(tween(900, easing = EaseInOut), RepeatMode.Reverse),
        label = "pulse_scale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f, targetValue = 0f,
        animationSpec = infiniteRepeatable(tween(900, easing = EaseInOut), RepeatMode.Reverse),
        label = "pulse_alpha"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        MapLibreMap(
            permissionsGranted = locationPermissionState.allPermissionsGranted,
            onMapReady = { mapInstance = it }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .align(Alignment.TopStart),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FilledIconButton(
                onClick = { sidebarOpen = !sidebarOpen },
                modifier = Modifier.size(42.dp),
                shape = CircleShape,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.97f)
                )
            ) {
                Icon(
                    imageVector = if (sidebarOpen) Icons.Default.Close else Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(18.dp)
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .clip(RoundedCornerShape(21.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.97f)),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = if (isUrdu) "کہاں جانا ہے؟" else "Where to?",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                        fontSize = 14.sp
                    )
                }
            }

            FilledIconButton(
                onClick = {
                    mapInstance?.locationComponent?.takeIf { it.isLocationComponentActivated }?.let { component ->
                        component.lastKnownLocation?.let { loc ->
                            mapInstance?.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(LatLng(loc.latitude, loc.longitude), 15.0)
                            )
                        }
                    }
                },
                modifier = Modifier.size(42.dp),
                shape = CircleShape,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.97f)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "My location",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = when {
                    micState == MicState.LISTENING && isUrdu -> "سن رہا ہوں..."
                    micState == MicState.LISTENING -> "Listening..."
                    isUrdu -> "بولیں"
                    else -> "Tap to speak"
                },
                color = if (micState == MicState.LISTENING)
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )

            Box(contentAlignment = Alignment.Center) {
                if (micState == MicState.LISTENING) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .scale(pulseScale)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.error.copy(alpha = pulseAlpha))
                    )
                }
                FilledIconButton(
                    onClick = { micState = if (micState == MicState.IDLE) MicState.LISTENING else MicState.IDLE },
                    modifier = Modifier.size(56.dp),
                    shape = CircleShape,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = if (micState == MicState.LISTENING)
                            MaterialTheme.colorScheme.error
                        else
                            MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = if (micState == MicState.LISTENING) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Voice toggle",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        if (sidebarOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { sidebarOpen = false }
            )

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(260.dp)
                    .align(Alignment.CenterStart)
                    .background(MaterialTheme.colorScheme.surface)
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 24.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Column {
                            Text(
                                text = if (isUrdu) "میرا اکاؤنٹ" else "My Account",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "HSMap",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                                fontSize = 12.sp
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                    )

                    SidebarItem(Icons.Default.Person, if (isUrdu) "پروفائل" else "Profile") {}
                    SidebarItem(Icons.Default.Place, if (isUrdu) "محفوظ مقامات" else "Saved Places") {}
                    SidebarItem(Icons.Default.DirectionsCar, if (isUrdu) "میری گاڑی" else "My Vehicle") {}
                    SidebarItem(Icons.Default.History, if (isUrdu) "حالیہ سفر" else "Recent Trips") {}
                    SidebarItem(Icons.Default.Settings, if (isUrdu) "ترتیبات" else "Settings") {}

                    Spacer(modifier = Modifier.weight(1f))

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                    )

                    SidebarItem(
                        icon = Icons.Default.Logout,
                        label = if (isUrdu) "لاگ آؤٹ" else "Logout",
                        tint = MaterialTheme.colorScheme.error,
                        onClick = {
                            sidebarOpen = false
                            navController.navigate("login") {
                                popUpTo("dashboard") { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
private fun MapLibreMap(
    permissionsGranted: Boolean,
    onMapReady: (LibMapInstance) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    fun hasLocationPermission() =
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    fun activateLocation(map: LibMapInstance, style: org.maplibre.android.maps.Style) {
        val locationComponent = map.locationComponent
        if (!locationComponent.isLocationComponentActivated) {
            locationComponent.activateLocationComponent(
                LocationComponentActivationOptions.builder(context, style)
                    .useDefaultLocationEngine(true)
                    .build()
            )
        }
        locationComponent.isLocationComponentEnabled = true
        locationComponent.cameraMode = CameraMode.TRACKING
        locationComponent.renderMode = RenderMode.COMPASS
        locationComponent.lastKnownLocation?.let { loc ->
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(loc.latitude, loc.longitude), 15.0))
        }
    }

    val mapView = remember {
        MapLibre.getInstance(context)
        MapView(context).apply {
            getMapAsync { map ->
                map.uiSettings.apply {
                    isLogoEnabled = false
                    isAttributionEnabled = false
                    isCompassEnabled = false
                }
                map.setStyle(MAP_STYLE_URL) { style ->
                    if (permissionsGranted && hasLocationPermission()) {
                        activateLocation(map, style)
                    }
                }
                onMapReady(map)
            }
        }
    }

    LaunchedEffect(permissionsGranted) {
        if (!permissionsGranted || !hasLocationPermission()) return@LaunchedEffect
        mapView.getMapAsync { map ->
            val locationComponent = map.locationComponent
            if (locationComponent.isLocationComponentActivated && locationComponent.isLocationComponentEnabled) return@getMapAsync
            map.getStyle { style -> activateLocation(map, style) }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE  -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START   -> mapView.onStart()
                Lifecycle.Event.ON_RESUME  -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE   -> mapView.onPause()
                Lifecycle.Event.ON_STOP    -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer)  }
    }

    AndroidView(factory = { mapView }, modifier = Modifier.fillMaxSize())
}

@Composable
private fun SidebarItem(
    icon: ImageVector,
    label: String,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint.copy(alpha = 0.65f),
            modifier = Modifier.size(20.dp)
        )
        Text(text = label, color = tint, fontSize = 15.sp)
    }
}