package com.example.shouldiski.ui.search

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.shouldiski.databinding.FragmentSearchBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    private var selectedDate=LocalDate.of(1500, 0 + 1, 1)

    private val viewModel: ShareViewModel by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Observe the LiveData for room availability data for debug
        viewModel.roomAvailabilityData.observe(viewLifecycleOwner, Observer { data ->
            binding.resultTextView.text = data
        })

        binding.dateButton.setOnClickListener {
            showDatePickerDialog()
        }

        binding.searchButton.setOnClickListener {
            if (binding.destinationEditText.text.isBlank())
            {
                Toast.makeText(requireContext(),
                    "Please Enter Destination",
                    Toast.LENGTH_SHORT).show()
            }
            else if (selectedDate == LocalDate.of(1500, 0 + 1, 1))
            {
                Toast.makeText(requireContext(),
                    "Please Select Date",
                    Toast.LENGTH_SHORT).show()
            }
            else {
                viewModel.submitData(binding.destinationEditText.text.toString(), selectedDate)
            }
        }
        return root
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(requireContext(),
            { _, year, month, dayOfMonth ->
                // Format the date and set it to the TextView
                selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val formattedDate = selectedDate.format(formatter)
                binding.dateTextview.text = "Selected Ski trip Date: $formattedDate"
            }, 2023, 11, 30)
        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
