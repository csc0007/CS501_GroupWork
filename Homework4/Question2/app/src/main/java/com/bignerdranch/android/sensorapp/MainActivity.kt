package com.bignerdranch.android.sensorapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.sensorapp.R

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    private lateinit var sensitivitySeekBar: SeekBar
    private lateinit var sensitivityTextView: TextView

    private var lastX: Float? = null
    private var lastY: Float? = null
    private var lastZ: Float? = null

    // Default sensitivity value.
    private var sensitivity: Float = 50.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        sensitivitySeekBar = findViewById(R.id.sensitivitySeekBar)
        sensitivityTextView = findViewById(R.id.sensitivityTextView)

        sensitivitySeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sensitivity = progress.toFloat()
                sensitivityTextView.text = "Sensitivity: $sensitivity"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (lastX == null || lastY == null || lastZ == null) {
                lastX = it.values[0]
                lastY = it.values[1]
                lastZ = it.values[2]
                return
            }

            val deltaX = Math.abs(lastX!! - it.values[0])
            val deltaY = Math.abs(lastY!! - it.values[1])
            val deltaZ = Math.abs(lastZ!! - it.values[2])

            if (deltaX > sensitivity || deltaY > sensitivity || deltaZ > sensitivity) {
                Toast.makeText(this, "Significant Movement Detected!", Toast.LENGTH_SHORT).show()
            }

            lastX = it.values[0]
            lastY = it.values[1]
            lastZ = it.values[2]
        }
    }
}
