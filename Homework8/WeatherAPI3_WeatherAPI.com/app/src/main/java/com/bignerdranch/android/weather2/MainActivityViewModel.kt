package com.bignerdranch.android.weather2

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class MainActivityViewModel : ViewModel() {
    private val client = OkHttpClient()
    val weatherData = MutableLiveData<String>()

    fun fetchWeatherForecast(cityName: String, date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = makeApiRequest(cityName, date)
                if (response.isNotEmpty()) {
                    val parsedData = parseWeatherData(response, date)
                    withContext(Dispatchers.Main) {
                        weatherData.value = parsedData
                    }
                } else {
                    Log.e("API_CALL", "Response was empty")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    weatherData.value = "Error: ${e.message}"
                    Log.e("API_CALL", "Error: ${e.message}")
                }
            }
        }
    }

    private fun makeApiRequest(cityName: String, date: String): String {
        val request = Request.Builder()
            .url("https://weatherapi-com.p.rapidapi.com/forecast.json?q=$cityName&days=3&dt=$date")
            .get()
            .addHeader("X-RapidAPI-Key", "e0fb2d06ebmsh13b4199c11aa6c3p1d57a3jsn2277e5c7358d") // Replace with your API key
            .addHeader("X-RapidAPI-Host", "weatherapi-com.p.rapidapi.com")
            .build()
        val response = client.newCall(request).execute()
        return response.body?.string() ?: ""
    }

    private fun parseWeatherData(jsonData: String, date: String): String {
        try {
            val jsonObject = JSONObject(jsonData)
            val forecast = jsonObject.getJSONObject("forecast")
            val forecastDayArray = forecast.getJSONArray("forecastday")

            for (i in 0 until forecastDayArray.length()) {
                val dayObject = forecastDayArray.getJSONObject(i)
                if (dayObject.getString("date") == date) {
                    val day = dayObject.getJSONObject("day")
                    val avgTempC = day.getDouble("avgtemp_c")
                    val condition = day.getJSONObject("condition")
                    val text = condition.getString("text")
                    return "Weather: $text\nAverage Temperature: $avgTempC °C"
                }
            }

            return "No weather data available for the selected date"
        } catch (e: Exception) {
            Log.e("JSON_PARSE", "Error parsing JSON: ${e.message}")
            return "Error parsing JSON: ${e.message}"
        }
    }
}