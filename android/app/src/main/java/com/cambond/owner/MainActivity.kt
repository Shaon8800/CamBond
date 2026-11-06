
package com.cambond.owner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

    private lateinit var status: TextView

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val cameraGranted = result[Manifest.permission.CAMERA] == true ||
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED

            if (cameraGranted) {
                startCameraService()
            } else {
                status.text = "Camera permission denied. Sharing is stopped."
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        status = TextView(this).apply {
            text = "CamBond Owner\nCamera is OFF"
            textSize = 18f
            setPadding(24, 32, 24, 24)
        }

        val start = Button(this).apply {
            text = "Start background camera"
            setOnClickListener {
                requestCameraPermission()
            }
        }

        val stop = Button(this).apply {
            text = "Stop camera"
            setOnClickListener {
                startService(
                    Intent(
                        this@MainActivity,
                        CameraForegroundService::class.java
                    ).setAction(CameraForegroundService.ACTION_STOP)
                )
                status.text = "Camera stopped."
            }
        }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 40, 24, 24)
            addView(status)
            addView(start)
            addView(stop)
        }

        setContentView(layout)
    }

    private fun requestCameraPermission() {
        val permissions = mutableListOf(Manifest.permission.CAMERA)

        if (Build.VERSION.SDK_INT >= 33) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        permissionLauncher.launch(permissions.toTypedArray())
    }

    private fun startCameraService() {
        val intent = Intent(this, CameraForegroundService::class.java)
        ContextCompat.startForegroundService(this, intent)
        status.text = "Camera service starting. Keep the notification visible."
    }
}
