package com.bignerdranch.android.flashapp

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import android.widget.Toast
import android.util.Log
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {
    private lateinit var cameraManager: CameraManager
    private var isFlashOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        val flashlightSwitch = findViewById<Switch>(R.id.flashlightSwitch)
        val actionTextBox = findViewById<EditText>(R.id.actionTextBox)

        flashlightSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                turnOnFlashlight()
            } else {
                turnOffFlashlight()
            }
        }

        actionTextBox.addTextChangedListener { text ->
            when (text.toString().toUpperCase()) {
                "ON" -> {
                    flashlightSwitch.isChecked = true
                    turnOnFlashlight()
                }
                "OFF" -> {
                    flashlightSwitch.isChecked = false
                    turnOffFlashlight()
                }
            }
        }

        val gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean {
                Log.d("Gesture", "onDown detected")
                return true
            }
            override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
                Log.d("Gesture", "onScroll detected with distanceY: $distanceY")
                return true
            }



            override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                if (e1 == null || e2 == null) return false

                val deltaY = e2.y - e1.y
                Log.d("Gesture", "Fling detected with deltaY: $deltaY and velocityY: $velocityY")

                // Checking if the fling is significant both in terms of speed and distance
                if (Math.abs(velocityY) > 1000 && Math.abs(deltaY) > 100) {
                    if (deltaY < 0) { // fling upwards
                        flashlightSwitch.isChecked = true
                    } else { // fling downwards
                        flashlightSwitch.isChecked = false
                    }
                    return true
                }
                return false
            }
        })

        findViewById<LinearLayout>(R.id.mainLayout).setOnTouchListener { _, event ->
            Log.d("Gesture", "Touch event detected")
            gestureDetector.onTouchEvent(event)
            true
        }
    }

    private fun isFlashAvailable(): Boolean {
        try {
            for (id in cameraManager.cameraIdList) {
                val characteristics = cameraManager.getCameraCharacteristics(id)
                val hasFlash = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
                if (hasFlash == true) {
                    return true
                }
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
        return false
    }

    private fun getCameraWithFlash(): String? {
        try {
            for (id in cameraManager.cameraIdList) {
                val characteristics = cameraManager.getCameraCharacteristics(id)
                val hasFlash = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
                if (hasFlash == true) {
                    return id
                }
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
        return null
    }

    private fun turnOnFlashlight() {
        if (!isFlashAvailable()) {
            showToast("Flashlight not available on this device.")
            return
        }
        val cameraId = getCameraWithFlash()
        if (cameraId == null) {
            showToast("No camera with flashlight available.")
            return
        }
        try {
            cameraManager.setTorchMode(cameraId, true)
            isFlashOn = true
        } catch (e: CameraAccessException) {
            e.printStackTrace()
            showToast("Error accessing the camera.")
        }
    }

    private fun turnOffFlashlight() {
        if (!isFlashAvailable()) {
            return
        }
        val cameraId = getCameraWithFlash()
        if (cameraId == null) {
            return
        }
        try {
            cameraManager.setTorchMode(cameraId, false)
            isFlashOn = false
        } catch (e: CameraAccessException) {
            e.printStackTrace()
            showToast("Error accessing the camera.")
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
