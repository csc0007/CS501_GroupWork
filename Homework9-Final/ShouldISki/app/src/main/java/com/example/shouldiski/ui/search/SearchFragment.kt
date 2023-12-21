package com.example.shouldiski.ui.search

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.shouldiski.databinding.FragmentSearchBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var locationManager: LocationManager
    private var locationListener: LocationListener? = null

    @RequiresApi(Build.VERSION_CODES.O)
    private var selectedDate=LocalDate.of(1500, 0 + 1, 1)

    private val viewModel: ShareViewModel by activityViewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Observe the LiveData for room availability data for debug
        viewModel.roomAvailabilityData.observe(viewLifecycleOwner, Observer { data ->
            binding.hotelDebug.text = data
        })

        viewModel.snowConditionLiveData.observe(viewLifecycleOwner, Observer { data ->
            binding.snowDebug.text = data
        })

        binding.dateButton.setOnClickListener {
            showDatePickerDialog()
        }

        binding.searchButton.setOnClickListener {
            val destination = binding.destinationEditText.text.toString()
            if (destination.isBlank()) {
                Toast.makeText(requireContext(), "Please Enter Destination", Toast.LENGTH_SHORT).show()
            } else if (selectedDate == LocalDate.of(1500, 1, 1)) {
                Toast.makeText(requireContext(), "Please Select Date", Toast.LENGTH_SHORT).show()
            } else {
                val code = viewModel.submitData(destination,  selectedDate)
                if(code == 0) {
                    Toast.makeText(requireContext(), "This destination is not supported", Toast.LENGTH_SHORT).show()
                }else{
                    getCurrentLocation { location ->
                        val origin = "${location.latitude},${location.longitude}"
                        viewModel.getDrivingDirections(origin, destination)
                    }
                }

            }
        }
        return root
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(requireContext(),
            { _, year, month, dayOfMonth ->
                selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val formattedDate = selectedDate.format(formatter)
                binding.dateTextview.text = "Selected Ski trip Date: $formattedDate"
            }, LocalDate.now().year, LocalDate.now().monthValue - 1, LocalDate.now().dayOfMonth)
        datePickerDialog.show()
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(callback: (Location) -> Unit) {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            // Initialize the LocationManager
            locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    callback(location)
                    // Remove updates to avoid unnecessary battery usage
                    locationManager.removeUpdates(this)
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, locationListener!!)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation { location ->
                val origin = "${location.latitude},${location.longitude}"
                val destination = binding.destinationEditText.text.toString()
                viewModel.getDrivingDirections(origin, destination)
            }
        } else {
            Toast.makeText(requireContext(), "Location permission is required", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        // Remove location updates to avoid memory leak
        locationListener?.let { locationManager.removeUpdates(it) }
    }
}