package com.bignerdranch.android.flashlightapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GestureDetectorCompat

class MainActivity : AppCompatActivity(), GestureDetector.OnGestureListener {

    private lateinit var cameraManager: CameraManager
    private lateinit var gestureDetector: GestureDetectorCompat
    private lateinit var cameraId: String
    private val CAMERA_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        cameraId = cameraManager.cameraIdList[0]
        gestureDetector = GestureDetectorCompat(this, this)

        val flashlightSwitch = findViewById<Switch>(R.id.flashlightSwitch)
        val actionEditText = findViewById<EditText>(R.id.actionEditText)

        flashlightSwitch.setOnCheckedChangeListener { _, isChecked ->
            try {
                cameraManager.setTorchMode(cameraId, isChecked)
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }

        actionEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                when (s.toString().toUpperCase()) {
                    "ON" -> {
                        flashlightSwitch.isChecked = true
                    }
                    "OFF" -> {
                        flashlightSwitch.isChecked = false
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can proceed with flashlight operations
            } else {
                Toast.makeText(this, "Camera permission is required for flashlight.", Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event!!) || super.onTouchEvent(event)
    }

    override fun onDown(e: MotionEvent): Boolean {
        return false // Return true if you want to consume this event, false otherwise
    }

    override fun onShowPress(e: MotionEvent) {}

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return false
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent) {}


    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        val flashlightSwitch = findViewById<Switch>(R.id.flashlightSwitch)

        if (e1 != null && e2 != null) {
            if (e1.y > e2.y) {
                flashlightSwitch.isChecked = true
            } else if (e1.y < e2.y) {
                flashlightSwitch.isChecked = false
            }
        }
        return true
    }

}

