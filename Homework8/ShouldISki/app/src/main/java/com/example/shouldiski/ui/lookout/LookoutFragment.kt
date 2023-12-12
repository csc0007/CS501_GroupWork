package com.example.shouldiski.ui.lookout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
//////////////////////////////////////General Function/////////////////////////////////////////////////
        viewModel.resortName.observe(viewLifecycleOwner) { data ->
            binding.lookoutTopicTextView.text = data
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
