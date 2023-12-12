package com.example.shouldiski.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.time.format.DateTimeFormatter

const val API_KEY: String = ""

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
    }

    ////////////////////////////Hotel Availability///////////////////////////////////////////////////
    val roomAvailabilityData = MutableLiveData<String>()
    val hotelPercentage = MutableLiveData<Float>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun compareRoomAvailability(checkin: String, checkout: String, hotelID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val originalCount = getUniqueRoomCount(checkin, checkout, hotelID)
                val sixMonthsLaterCount = getUniqueRoomCount(addSixMonths(checkin), addSixMonths(checkout), hotelID)
                // compare with availability after six month (assume this is the empty status)
                val percentage = ((sixMonthsLaterCount - originalCount).toFloat() / sixMonthsLaterCount.toFloat()) * 100
                withContext(Dispatchers.Main) {
                    hotelPercentage.value = percentage
                    roomAvailabilityData.value = "Available rooms: $originalCount\n" +
                            "Available rooms after six months: $sixMonthsLaterCount\n" +
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
    private fun addSixMonths(date: String): String {
        val parsedDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
        return parsedDate.plusMonths(3).format(DateTimeFormatter.ISO_LOCAL_DATE)
    }

    //////////////////////////////////Snow Condition////////////////////////////////////////////////
    val snowConditionLiveData = MutableLiveData<String>()
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
                snowConditionLiveData.value = formatSnowCondition(snowCondition)
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

}