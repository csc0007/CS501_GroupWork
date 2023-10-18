package com.bignerdranch.android.sensor
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private lateinit var sensitivitySeekBar: SeekBar
    private lateinit var sensitivityLabel: TextView

    private var lastX: Float? = null
    private var lastY: Float? = null
    private var lastZ: Float? = null
    private var sensitivity: Int = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        sensitivitySeekBar = findViewById(R.id.sensitivitySeekBar)
        sensitivityLabel = findViewById(R.id.sensitivityLabel)

        sensitivitySeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sensitivity = progress
                sensitivityLabel.text = "Sensitivity: $sensitivity"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val x = it.values[0]
            val y = it.values[1]
            val z = it.values[2]

            if (lastX == null || lastY == null || lastZ == null) {
                lastX = x
                lastY = y
                lastZ = z
                return
            }

            val deltaX = abs(x - lastX!!)
            val deltaY = abs(y - lastY!!)
            val deltaZ = abs(z - lastZ!!)

            if (deltaX > sensitivity || deltaY > sensitivity || deltaZ > sensitivity) {
                Toast.makeText(this, "Significant movement detected!", Toast.LENGTH_SHORT).show()
                println("Movement in X: $deltaX, Y: $deltaY, Z: $deltaZ")
            }

            lastX = x
            lastY = y
            lastZ = z
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Nothing to do here
    }
}
