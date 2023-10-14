package com.example.fiveactivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fiveactivities.databinding.ActivityEastBinding

class EastActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEastBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEastBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}