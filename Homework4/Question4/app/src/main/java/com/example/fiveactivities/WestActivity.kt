package com.example.fiveactivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fiveactivities.databinding.ActivityWestBinding

class WestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWestBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}