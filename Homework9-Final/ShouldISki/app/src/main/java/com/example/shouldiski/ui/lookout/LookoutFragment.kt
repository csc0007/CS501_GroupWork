package com.example.shouldiski.ui.lookout

import LookoutDialogFragment
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.shouldiski.databinding.FragmentLookoutBinding
import com.example.shouldiski.ui.search.ShareViewModel
import androidx.core.graphics.drawable.DrawableCompat
import com.example.shouldiski.R

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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weatherTextView = view.findViewById<TextView>(R.id.weather)
        val weatherIconImageView = view.findViewById<ImageView>(R.id.weatherIconImageView)
        val distanceIconImageView = view.findViewById<ImageView>(R.id.distanceIconImageView)
        val durationIconImageView = view.findViewById<ImageView>(R.id.durationIconImageView)
////////////////////////////////////////Hotel API////////////////////////////////////////////////////////////
        var hotelInformation="No Data"
        viewModel.roomAvailabilityData.observe(viewLifecycleOwner) { data ->
            hotelInformation = data
            binding.debugHotelTextView.text = data
        }

        viewModel.hotelPercentage.observe(viewLifecycleOwner) { percentage ->
            val hotelDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_bed_24)
            val wrappedHotelDrawable = DrawableCompat.wrap(hotelDrawable!!).mutate()

            val crowdDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_groups_24)
            val wrappedCrowdDrawable = DrawableCompat.wrap(crowdDrawable!!).mutate()

            if (percentage>=0&&percentage<20)
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
            else if (percentage>=40)
            {
                DrawableCompat.setTint(wrappedHotelDrawable,
                    ContextCompat.getColor(requireContext(), R.color.red))
                binding.hotel.text = "Few Hotel"
                DrawableCompat.setTint(wrappedCrowdDrawable,
                    ContextCompat.getColor(requireContext(), R.color.red))
                binding.crowd.text = "Extremely Crowded"
            }
            else    //Hotel Query error message
            {
                DrawableCompat.setTint(wrappedHotelDrawable,
                    ContextCompat.getColor(requireContext(), R.color.red))
                binding.hotel.text = hotelInformation
                DrawableCompat.setTint(wrappedCrowdDrawable,
                    ContextCompat.getColor(requireContext(), R.color.red))
                binding.crowd.text = hotelInformation
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
            else if (data == 0)
            {
                DrawableCompat.setTint(wrappedSkiDrawable,
                    ContextCompat.getColor(requireContext(), R.color.red))
                binding.skiCondition.text = "No Fresh Snow"
            }
            else
            {
                DrawableCompat.setTint(wrappedSkiDrawable,
                    ContextCompat.getColor(requireContext(), R.color.red))
                binding.skiCondition.text = "Data Error"
            }

            binding.skiImageView.setImageDrawable(wrappedSkiDrawable)
        }
        var snowInformation= "No Data"
        viewModel.snowConditionLiveData.observe(viewLifecycleOwner) { data ->
            snowInformation = data
            binding.debugSnowTextView.text = snowInformation
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
        var weatherInformation= "No Data"
        viewModel.weatherData.observe(viewLifecycleOwner) { data ->
            weatherInformation = data
            binding.debugWeatherTextView.text = weatherInformation
        }
///////////////////////////////////Driving Direction API//////////////////////////////////////////////////
        viewModel.routeInfo.observe(viewLifecycleOwner) { data ->
            val (distance, duration) = parseRouteInfo(data)
            binding.traffic.text = distance
            binding.driveTime.text = duration

            updateIconColors(distance, duration)
        }
        var trafficInformation= "No Data"
        viewModel.routeInfo.observe(viewLifecycleOwner) { data ->
            trafficInformation = data
            binding.debugTrafficTextView.text = trafficInformation
        }
////////////////////////////////////Icon Click Event Handle//////////////////////////////////////////////
        binding.hotelImageView.setOnClickListener{
            val dialogFragment = LookoutDialogFragment()

            dialogFragment.arguments = Bundle().apply {
                putString("title", "Hotel Information")
                putString("mainText", hotelInformation)
            }
            dialogFragment.show(childFragmentManager, "hotelDialog")
        }

        binding.crowdImageView.setOnClickListener{
            val dialogFragment = LookoutDialogFragment()

            dialogFragment.arguments = Bundle().apply {
                putString("title", "Crowd Prediction")
                putString("mainText", hotelInformation)     //not changed yet
            }
            dialogFragment.show(childFragmentManager, "crowdDialog")
        }

        binding.skiImageView.setOnClickListener{
            val dialogFragment = LookoutDialogFragment()

            dialogFragment.arguments = Bundle().apply {
                putString("title", "Live Snow Condition Report")
                putString("mainText", snowInformation)
            }
            dialogFragment.show(childFragmentManager, "skiDialog")
        }

        binding.weatherIconImageView.setOnClickListener{
            val dialogFragment = LookoutDialogFragment()

            dialogFragment.arguments = Bundle().apply {
                putString("title", "Weather Forecast Report")
                putString("mainText", weatherInformation)
            }
            dialogFragment.show(childFragmentManager, "weatherDialog")
        }

        binding.distanceIconImageView.setOnClickListener{
            val dialogFragment = LookoutDialogFragment()

            dialogFragment.arguments = Bundle().apply {
                putString("title", "Distance Information Report")
                putString("mainText", trafficInformation)
            }
            dialogFragment.show(childFragmentManager, "distanceDialog")
        }

        binding.durationIconImageView.setOnClickListener{
            val dialogFragment = LookoutDialogFragment()

            dialogFragment.arguments = Bundle().apply {
                putString("title", "Duration Information Report")
                putString("mainText", trafficInformation)
            }
            dialogFragment.show(childFragmentManager, "durationDialog")
        }

//////////////////////////////////////General Function/////////////////////////////////////////////////
        viewModel.resortName.observe(viewLifecycleOwner) { data ->
            binding.lookoutTopicTextView.text = data
        }

        viewModel.recommendation.observe(viewLifecycleOwner) { data ->
            binding.overallScore.text = "Overall Score: ${String.format("%.2f", data)}"
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
    private fun parseRouteInfo(routeInfo: String): Pair<String, String> {
        // Extract distance and duration from the routeInfo string
        val distance = routeInfo.substringAfter("Distance: ").substringBefore(", Duration: ")
        val duration = routeInfo.substringAfter("Duration: ")
        return Pair(distance, duration)
    }

    private fun updateIconColors(distance: String, duration: String) {
        val distanceValue = distance.filter { it.isDigit() }.toIntOrNull() ?: 0
        val durationHours = duration.substringBefore(" hr").toIntOrNull() ?: 0

        val distanceColor = when {
            distanceValue <= 150 -> R.color.green
            distanceValue <= 400 -> R.color.orange
            else -> R.color.red
        }
        val durationColor = when {
            durationHours <= 5 -> R.color.green
            durationHours <= 8 -> R.color.orange
            else -> R.color.red
        }

        val distanceDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_directions_car_24)?.mutate()
        val durationDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_access_time_24)?.mutate()

        DrawableCompat.setTint(distanceDrawable!!, ContextCompat.getColor(requireContext(), distanceColor))
        DrawableCompat.setTint(durationDrawable!!, ContextCompat.getColor(requireContext(), durationColor))

        binding.distanceIconImageView.setImageDrawable(distanceDrawable)
        binding.durationIconImageView.setImageDrawable(durationDrawable)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
