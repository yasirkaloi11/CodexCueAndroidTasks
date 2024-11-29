package com.example.flashlightapp

import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlashlightApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashlightApp() {
    val context = LocalContext.current
    val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

    // Get the ID of the camera with a flashlight
    val cameraId = cameraManager.cameraIdList.firstOrNull { id ->
        cameraManager.getCameraCharacteristics(id).get(
            android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE
        ) == true
    }

    if (cameraId == null) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "Flashlight not supported on this device")
        }
        return
    }

    var isFlashlightOn by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Flashlight App") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    cameraManager.setTorchMode(cameraId, true)
                    isFlashlightOn = true
                },
                enabled = !isFlashlightOn,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("On")
            }

            Button(
                onClick = {
                    cameraManager.setTorchMode(cameraId, false)
                    isFlashlightOn = false
                },
                enabled = isFlashlightOn,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Off")
            }
        }
    }
}
