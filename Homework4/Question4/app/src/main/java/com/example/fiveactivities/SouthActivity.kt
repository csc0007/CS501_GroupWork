package com.example.fiveactivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fiveactivities.databinding.ActivitySouthBinding

class SouthActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySouthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySouthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}