package com.example.shouldiski.ui.lookout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.material.Card
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.shouldiski.databinding.FragmentLookoutBinding
import com.example.shouldiski.ui.search.ShareViewModel
import androidx.core.graphics.drawable.DrawableCompat
import com.example.shouldiski.R
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.style.TextAlign


class LookoutFragment : Fragment() {

    private var _binding: FragmentLookoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ShareViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLookoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weatherTextView = view.findViewById<TextView>(R.id.weather)
        val weatherIconImageView = view.findViewById<ImageView>(R.id.weatherIconImageView)
////////////////////////////////////////Hotel API////////////////////////////////////////////////////////////
        viewModel.roomAvailabilityData.observe(viewLifecycleOwner) { data ->
            binding.debugHotelTextView.text = data
        }

        viewModel.hotelPercentage.observe(viewLifecycleOwner) { percentage ->
            val hotelDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_bed_24)
            val wrappedHotelDrawable = DrawableCompat.wrap(hotelDrawable!!).mutate()

            val crowdDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_groups_24)
            val wrappedCrowdDrawable = DrawableCompat.wrap(crowdDrawable!!).mutate()

            if (percentage<20)
            {
                DrawableCompat.setTint(wrappedHotelDrawable,
                    ContextCompat.getColor(requireContext(), R.color.green))
                binding.hotel.text = "A Lot Occupancy"
                DrawableCompat.setTint(wrappedCrowdDrawable,
                    ContextCompat.getColor(requireContext(), R.color.green))
                binding.crowd.text = "Few People"
            }
            else if (percentage>=20&&percentage<40)
            {
                DrawableCompat.setTint(wrappedHotelDrawable,
                    ContextCompat.getColor(requireContext(), R.color.orange))
                binding.hotel.text = "Medium Occupancy"
                DrawableCompat.setTint(wrappedCrowdDrawable,
                    ContextCompat.getColor(requireContext(), R.color.orange))
                binding.crowd.text = "Expect Crowded"
            }
            else
            {
                DrawableCompat.setTint(wrappedHotelDrawable,
                    ContextCompat.getColor(requireContext(), R.color.red))
                binding.hotel.text = "Few Hotel"
                DrawableCompat.setTint(wrappedCrowdDrawable,
                    ContextCompat.getColor(requireContext(), R.color.red))
                binding.crowd.text = "Extremely Crowded"
            }

            binding.hotelImageView.setImageDrawable(wrappedHotelDrawable)
            binding.crowdImageView.setImageDrawable(wrappedCrowdDrawable)
        }
///////////////////////////////////Snow Condition API//////////////////////////////////////////////////

        viewModel.freshSnowLiveData.observe(viewLifecycleOwner) { data ->

            val skiDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_downhill_skiing_24)
            val wrappedSkiDrawable = DrawableCompat.wrap(skiDrawable!!).mutate()

            if(data>15)
            {
                DrawableCompat.setTint(wrappedSkiDrawable,
                    ContextCompat.getColor(requireContext(), R.color.green))
                binding.skiCondition.text = "Excellent Snow"
            }
            else if (data>0&&data<=15)
            {
                DrawableCompat.setTint(wrappedSkiDrawable,
                    ContextCompat.getColor(requireContext(), R.color.orange))
                binding.skiCondition.text = "Some Snow"
            }
            else
            {
                DrawableCompat.setTint(wrappedSkiDrawable,
                    ContextCompat.getColor(requireContext(), R.color.red))
                binding.skiCondition.text = "No Fresh Snow"
            }

            binding.skiImageView.setImageDrawable(wrappedSkiDrawable)
        }

        viewModel.snowConditionLiveData.observe(viewLifecycleOwner) { data ->
            binding.debugSnowTextView.text = data
        }
    ///////////////////////////////////Weather Forecast API//////////////////////////////////////////////////

        viewModel.weatherData.observe(viewLifecycleOwner) { data ->

            // Update UI with weather data
            weatherTextView.text = data

            // Extract weather condition from the data
            val weatherCondition = extractWeatherCondition(data)

            // Change icon color based on weather condition
            val color = when {
                weatherCondition.contains("sunny", ignoreCase = true) || weatherCondition.contains("cloudy", ignoreCase = true) -> R.color.green
                weatherCondition.contains("rain", ignoreCase = true) || weatherCondition.contains("freezing", ignoreCase = true) || weatherCondition.contains("fog", ignoreCase = true)-> R.color.red
                weatherCondition.contains("snow", ignoreCase = true) || weatherCondition.contains("mist", ignoreCase = true)-> R.color.orange
                else -> R.color.white
            }
            changeIconColor(color)
        }
//////////////////////////////////////General Function/////////////////////////////////////////////////
        viewModel.resortName.observe(viewLifecycleOwner) { data ->
            binding.lookoutTopicTextView.text = data
        }

        val composeView = binding.composeView
        composeView.setContent {
            // State to control the dialog visibility
            val showDialog = remember { mutableStateOf(false) }

            if (showDialog.value) {
                MinimalDialog(onDismissRequest = { showDialog.value = false })
            }
        }
        binding.hotelImageView.setOnClickListener{
            composeView.setContent {
                val showDialog = remember { mutableStateOf(true) }
                if (showDialog.value) {
                    MinimalDialog(onDismissRequest = { showDialog.value = false })
                }
            }
        }

    }

    @Composable
    fun MinimalDialog(onDismissRequest: () -> Unit) {
        Dialog(onDismissRequest = { onDismissRequest() }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Text(
                    text = "This is a minimal dialog",
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
    // Extract the weather condition from the weather data string
    private fun extractWeatherCondition(data: String): String {
        return data.substringAfter("Weather: ").substringBefore("\n")
    }
    // Change the color of the baseline_brightness_7_24 drawable
    private fun changeIconColor(colorResId: Int) {
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_brightness_7_24)?.mutate()
        val color = ContextCompat.getColor(requireContext(), colorResId)
        DrawableCompat.setTint(drawable!!, color)
        binding.weatherIconImageView.setImageDrawable(drawable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



