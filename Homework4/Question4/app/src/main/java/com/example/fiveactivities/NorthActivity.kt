package com.example.fiveactivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fiveactivities.databinding.ActivityNorthBinding

class NorthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNorthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNorthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}