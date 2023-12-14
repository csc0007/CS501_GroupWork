package com.bignerdranch.android.mapapi

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bignerdranch.android.mapapi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonGetDirections.setOnClickListener {
            val origin = binding.editTextOrigin.text.toString()
            val destination = binding.editTextDestination.text.toString()
            viewModel.getDrivingDirections(origin, destination)
        }

        viewModel.routeInfo.observe(this, Observer { data ->
            binding.textViewResult.text = data
        })
    }
}
