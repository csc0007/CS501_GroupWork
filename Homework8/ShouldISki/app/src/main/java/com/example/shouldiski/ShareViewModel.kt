package com.example.shouldiski.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.time.format.DateTimeFormatter

const val API_KEY: String = "054999d8c2mshb298a4b5c4ddea0p15eb49jsnabf9c5983234" //API key for hotelAPI and snow condition API

class ShareViewModel : ViewModel() {

    private val client = OkHttpClient()

    /////////////////////////////////////General API Functions///////////////////////////////////////

    val resortName = MutableLiveData<String>()

    // Function to be called when data is submitted from the UI
    @RequiresApi(Build.VERSION_CODES.O)
    fun submitData(destination: String, date: LocalDate?) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val checkInDate = date?.format(formatter).toString()
        val nextday=date?.plusDays(1)       //set default hotel check for 1 day
        val checkOutDate = nextday?.format(formatter).toString()
        if (destination=="Stowe")
        {
            resortName.value="Stowe Mountain Resort"
            //hotelID is hard coded here, there will be a database to save this information
            compareRoomAvailability(checkInDate, checkOutDate,"191981")
            fetchSnowCondition(destination)
        }
        if (date != null) {
            fetchWeatherForecast(destination, date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        }
    }

    ////////////////////////////Hotel Availability///////////////////////////////////////////////////
    val roomAvailabilityData = MutableLiveData<String>()
    val hotelPercentage = MutableLiveData<Float>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun compareRoomAvailability(checkin: String, checkout: String, hotelID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val originalCount = getUniqueRoomCount(checkin, checkout, hotelID)
                val monthsLaterCount = getUniqueRoomCount(addMonths(checkin), addMonths(checkout), hotelID)
                // compare with availability after six month (assume this is the empty status)
                val percentage = ((monthsLaterCount - originalCount).toFloat() / monthsLaterCount.toFloat()) * 100
                withContext(Dispatchers.Main) {
                    hotelPercentage.value = percentage
                    roomAvailabilityData.value = "Available rooms: $originalCount\n" +
                            "Available rooms after three months: $monthsLaterCount\n" +
                            "Percentage of rooms booked: ${"%.2f".format(percentage)}%"
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    roomAvailabilityData.value = "Error: ${e.message}"
                }
            }
        }
    }

    private suspend fun getUniqueRoomCount(checkin: String, checkout: String, hotelID: String): Int {
        val request = Request.Builder()
            .url("https://booking-com-api3.p.rapidapi.com/booking/blockAvailability?room1=A&checkin=$checkin" +
                    "&checkout=$checkout&hotel_ids=$hotelID&guest_cc=us")
            .addHeader("Accept", "application/json")
            .addHeader("X-RapidAPI-Key", API_KEY)
            .addHeader("X-RapidAPI-Host", "booking-com-api3.p.rapidapi.com")
            .build()

        val response = client.newCall(request).execute()
        val jsonData = JSONObject(response.body?.string() ?: "")
        val results = jsonData.getJSONArray("result")
        val uniqueRoomIds = mutableSetOf<Int>()
        for (i in 0 until results.length()) {
            val blocks = results.getJSONObject(i).getJSONArray("block")
            for (j in 0 until blocks.length()) {
                val roomId = blocks.getJSONObject(j).getInt("room_id")
                uniqueRoomIds.add(roomId)
            }
        }
        return uniqueRoomIds.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addMonths(date: String): String {
        val parsedDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
        return parsedDate.plusMonths(3).format(DateTimeFormatter.ISO_LOCAL_DATE)
    }

    //////////////////////////////////Snow Condition////////////////////////////////////////////////
    val snowConditionLiveData = MutableLiveData<String>()
    val snowConditionStructureLiveData = MutableLiveData<SnowCondition>()
    val freshSnowLiveData = MutableLiveData<Int>()
    val errorLiveData = MutableLiveData<String>()

    data class SnowCondition(
        val topSnowDepth: String,
        val botSnowDepth: String,
        val freshSnowfall: String,
        val lastSnowfallDate: String
    )

    private fun fetchSnowCondition(resortName: String) {

        viewModelScope.launch {
            try {
                val snowCondition = getSnowCondition(resortName)
                snowConditionStructureLiveData.value = snowCondition
                snowConditionLiveData.value = formatSnowCondition(snowCondition)
                freshSnowLiveData.value = snowCondition.freshSnowfall.replace("cm", "").toInt()
            } catch (e: Exception) {
                errorLiveData.postValue(e.message)
            }
        }
    }

    private suspend fun getSnowCondition(resortName: String): SnowCondition = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("https://ski-resort-forecast.p.rapidapi.com/$resortName/snowConditions")
            .addHeader("X-RapidAPI-Key", API_KEY)
            .addHeader("X-RapidAPI-Host", "ski-resort-forecast.p.rapidapi.com")
            .build()

        val response = client.newCall(request).execute()
        val jsonData = JSONObject(response.body?.string() ?: "")

        // Choose result format, 'metric' or 'imperial'
        val snowData = jsonData.getJSONObject("metric")

        return@withContext SnowCondition(
            topSnowDepth = snowData.getString("topSnowDepth"),
            botSnowDepth = snowData.getString("botSnowDepth"),
            freshSnowfall = snowData.getString("freshSnowfall"),
            lastSnowfallDate = snowData.getString("lastSnowfallDate")
        )
    }

    private fun formatSnowCondition(snowCondition: SnowCondition): String {
        return "Top Snow Depth: ${snowCondition.topSnowDepth}\n" +
                "Bottom Snow Depth: ${snowCondition.botSnowDepth}\n" +
                "Fresh Snowfall: ${snowCondition.freshSnowfall}\n" +
                "Last Snowfall Date: ${snowCondition.lastSnowfallDate}"
    }
    ////////////////////////////Weather Forecast///////////////////////////////////////////////////
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
                    return " $text\n $avgTempC °C"
                }
            }

            return "No weather data available for the selected date"
        } catch (e: Exception) {
            Log.e("JSON_PARSE", "Error parsing JSON: ${e.message}")
            return "Error parsing JSON: ${e.message}"
        }
    }
}