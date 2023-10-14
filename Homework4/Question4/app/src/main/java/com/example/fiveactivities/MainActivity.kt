package com.example.fiveactivities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import com.example.fiveactivities.databinding.ActivityMainBinding
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import java.lang.Math.sqrt
import java.util.Objects

private const val TAG = "MainActivity"

const val MIN_FLING_DISTANCE = 120 // Minimum Fling Distance
const val MIN_FLING_VELOCITY = 100 // Minimum Fling Speed
const val MIN_ACCELERATION = 2 // Minimum Acceleration Threshold

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var gestureDetector: GestureDetector

    // Declaring sensorManager and acceleration constants
    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Getting the Sensor Manager instance
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        Objects.requireNonNull(sensorManager)!!
            .registerListener(sensorListener, sensorManager!!
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)

        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH

        gestureDetector = GestureDetector(this,
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onFling(
                    event1: MotionEvent?,
                    event2: MotionEvent,
                    velocityX: Float,
                    velocityY: Float
                ): Boolean {
                    if (event1 != null) {
                        if (event1.y - event2.y > MIN_FLING_DISTANCE && Math.abs(velocityY) > MIN_FLING_VELOCITY) {
                            // Detected upward fling
                            onUpwardFling()
                        }
                        if (event2.y - event1.y > MIN_FLING_DISTANCE && Math.abs(velocityY) > MIN_FLING_VELOCITY) {
                            // Detected downward fling
                            onDownwardFling()
                        }
                        if (event2.x - event1.x > MIN_FLING_DISTANCE && Math.abs(velocityX) > MIN_FLING_VELOCITY) {
                            // Left to right swipe
                            onRightFling()
                        }
                        if (event1.x - event2.x > MIN_FLING_DISTANCE && Math.abs(velocityX) > MIN_FLING_VELOCITY) {
                            // Right to left swipe
                            onLeftFling()
                        }
                        return true
                    }
                    else
                        return false
            }
        })
    }

    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {

            // Fetching x,y,z values
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = currentAcceleration

            // Getting current accelerations
            // with the help of fetched x,y,z values
            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta

            // Display a Toast message if
            // acceleration value is over 2
            if (acceleration > MIN_ACCELERATION) {
                val shakeAnimation = AnimationUtils.loadAnimation(this@MainActivity, R.anim.shake)
                val textView = findViewById<TextView>(R.id.home_textview)
                textView.startAnimation(shakeAnimation)
            }
        }
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    private fun onUpwardFling() {
           val northIntent = Intent(this, NorthActivity::class.java)
           startActivity(northIntent)
    }
    private fun onDownwardFling() {
        val southIntent = Intent(this, SouthActivity::class.java)
        startActivity(southIntent)
    }
    private fun onRightFling() {
        val eastIntent = Intent(this, EastActivity::class.java)
        startActivity(eastIntent)
    }
    private fun onLeftFling() {
        val westIntent = Intent(this, WestActivity::class.java)
        startActivity(westIntent)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            gestureDetector.onTouchEvent(event)
        }
        return super.onTouchEvent(event)
    }

    override fun onResume() {
        sensorManager?.registerListener(sensorListener, sensorManager!!.getDefaultSensor(
            Sensor .TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
        )
        super.onResume()
    }

    override fun onPause() {
        sensorManager!!.unregisterListener(sensorListener)
        super.onPause()
    }
}