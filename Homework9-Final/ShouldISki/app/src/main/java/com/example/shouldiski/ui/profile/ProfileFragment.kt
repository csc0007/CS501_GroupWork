package com.example.shouldiski.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.shouldiski.SQLiteDatabase.PreferenceDatabase.Preference
import com.example.shouldiski.SQLiteDatabase.PreferenceDatabase.PreferenceDatabaseHandler
import com.example.shouldiski.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!

    private val dbHandler by lazy { PreferenceDatabaseHandler(requireContext()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadPreferences() //set switch states according to database

        binding.saveButton.setOnClickListener{
            var preference = Preference(
                powderSnow = binding.preferenceSwitchPowdersnow.isChecked.toInt(),
                groomedSnow = binding.preferenceSwitchGroomedsnow.isChecked.toInt(),
                coldWeather = binding.preferenceSwitchCold.isChecked.toInt(),
                warmWeather = binding.preferenceSwitchWarm.isChecked.toInt(),
                snowWeather = binding.preferenceSwitchSnowing.isChecked.toInt(),
                clearWeather = binding.preferenceSwitchClear.isChecked.toInt(),
                carveSkill = binding.preferenceSwitchCarve.isChecked.toInt(),
                parkSkill = binding.preferenceSwitchPark.isChecked.toInt(),
                levelP = binding.radioButtonBeginer.isChecked.toInt() +
                        binding.radioButtonIntermediate.isChecked.toInt() * 2 +
                        binding.radioButtonExpert.isChecked.toInt() * 3
            )
            dbHandler.updatePreference(preference)
        }
    }

    private fun Boolean.toInt() = if (this) 1 else 0

    private fun loadPreferences() {
        val preferences = dbHandler.getPreference()

        // Set the switch states based on the retrieved preferences
        binding.preferenceSwitchPowdersnow.isChecked = (preferences.powderSnow == 1)
        binding.preferenceSwitchGroomedsnow.isChecked = (preferences.groomedSnow == 1)
        binding.preferenceSwitchCold.isChecked = (preferences.coldWeather == 1)
        binding.preferenceSwitchWarm.isChecked = (preferences.warmWeather == 1)
        binding.preferenceSwitchSnowing.isChecked = (preferences.snowWeather == 1)
        binding.preferenceSwitchClear.isChecked = (preferences.clearWeather == 1)
        binding.preferenceSwitchCarve.isChecked = (preferences.carveSkill == 1)
        binding.preferenceSwitchPark.isChecked = (preferences.parkSkill == 1)
        if (preferences.levelP==1) binding.radioButtonBeginer.isChecked = true
        else if (preferences.levelP==2) binding.radioButtonIntermediate.isChecked = true
        else if (preferences.levelP==3) binding.radioButtonExpert.isChecked = true

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}