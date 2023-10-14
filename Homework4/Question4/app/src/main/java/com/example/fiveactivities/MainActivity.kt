package com.example.fiveactivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.fiveactivities.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.northButton.setOnClickListener{ _: View ->
            //Do something
            val northIntent = Intent(this, NorthActivity::class.java)
            startActivity(northIntent)
        }
        binding.westButton.setOnClickListener{ _: View ->
            //Do something
            val westIntent = Intent(this, WestActivity::class.java)
            startActivity(westIntent)
        }
        binding.eastButton.setOnClickListener{ _: View ->
            //Do something
            val eastIntent = Intent(this, EastActivity::class.java)
            startActivity(eastIntent)
        }
        binding.southButton.setOnClickListener{ _: View ->
            //Do something
            val southIntent = Intent(this, SouthActivity::class.java)
            startActivity(southIntent)
        }

    }



}