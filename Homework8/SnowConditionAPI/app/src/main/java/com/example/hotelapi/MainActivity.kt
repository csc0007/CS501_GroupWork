package com.example.hotelapi

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.example.hotelapi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainActivityViewModel.snowConditionLiveData.observe(this, Observer { snowCondition ->
            binding.textview1.text = formatSnowCondition(snowCondition)
        })

        mainActivityViewModel.errorLiveData.observe(this, Observer { errorMessage ->
            binding.textview1.text = errorMessage
        })

        //call to fetch snow condition data
        mainActivityViewModel.fetchSnowCondition("Stowe")
    }

    private fun formatSnowCondition(snowCondition: MainActivityViewModel.SnowCondition): String {
        return "Top Snow Depth: ${snowCondition.topSnowDepth}\n" +
                "Bottom Snow Depth: ${snowCondition.botSnowDepth}\n" +
                "Fresh Snowfall: ${snowCondition.freshSnowfall}\n" +
                "Last Snowfall Date: ${snowCondition.lastSnowfallDate}"
    }
}

