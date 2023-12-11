package com.example.shouldiski.ui.lookout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
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

        viewModel.roomAvailabilityData.observe(viewLifecycleOwner, Observer { data ->
            binding.debugTextView.text = data
        })

        viewModel.hotelPercentage.observe(viewLifecycleOwner, Observer { data ->
            val hoteldrawable = binding.hotelImageView.drawable
            val wrappedDrawable = DrawableCompat.wrap(hoteldrawable).mutate()

            // Your color tint logic
            val color = when {
                data < 30 -> ContextCompat.getColor(requireContext(), R.color.green)
                data in 30..49 -> ContextCompat.getColor(requireContext(), R.color.orange)
                else -> ContextCompat.getColor(requireContext(), R.color.red)
            }

            DrawableCompat.setTint(wrappedDrawable, color)
            binding.hotelImageView.setImageDrawable(wrappedDrawable)
        })

        viewModel.resortName.observe(viewLifecycleOwner, Observer { data ->
            binding.lookoutTopicTextView.text = data
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
