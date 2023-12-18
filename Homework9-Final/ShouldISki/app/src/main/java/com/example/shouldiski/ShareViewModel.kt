package com.example.shouldiski.ui.search

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import java.time.LocalDate
import android.os.Build
import android.provider.Settings.Global.putInt
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.shouldiski.SQLiteDatabase.ResortDatabase.ResortDatabaseHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.time.format.DateTimeFormatter

const val API_KEY: String = "054999d8c2mshb298a4b5c4ddea0p15eb49jsnabf9c5983234" //API key for hotelAPI and snow condition API

class ShareViewModel(application: Application) : AndroidViewModel(application) {

    private val client = OkHttpClient()

    private val dbHandler = ResortDatabaseHandler(application)

    /////////////////////////////////////General API Functions///////////////////////////////////////

    val resortName = MutableLiveData<String>()

    // Function to be called when data is submitted from the UI
    // return 0 if input destination is not in database
    @RequiresApi(Build.VERSION_CODES.O)
    fun submitData(destination: String, date: LocalDate?): Int {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val checkInDate = date?.format(formatter).toString()
        val nextday=date?.plusDays(1)       //set default hotel check for 1 day
        val checkOutDate = nextday?.format(formatter).toString()
        if (dbHandler.checkDestination(destination))
        {
            val hotelId = dbHandler.getHotelId(destination).toString()
            resortName.value=dbHandler.getResortName(destination).toString()
            //hotelID is hard coded here, there will be a database to save this information
            compareRoomAvailability(checkInDate, checkOutDate,hotelId)
            fetchSnowCondition(destination)
            return 1
        }
        else return 0

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
                    val percentage = -1
                    hotelPercentage.value = percentage.toFloat()
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
                if(snowCondition.freshSnowfall == "null")
                {
                    freshSnowLiveData.value = 0
                }
                else
                {
                    freshSnowLiveData.value = snowCondition.freshSnowfall.replace("cm", "").toInt()
                }
            } catch (e: Exception) {
                errorLiveData.postValue(e.message)
            }
        }
    }

    private suspend fun getSnowCondition(resortName: String): SnowCondition = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("https://ski-resort-forecast.p.rapidapi.com/$resortName/snowConditions?units=m")
            .addHeader("X-RapidAPI-Key", API_KEY)
            .addHeader("X-RapidAPI-Host", "ski-resort-forecast.p.rapidapi.com")
            .build()

        val response = client.newCall(request).execute()
        //val jsonData = JSONObject(response.body?.string() ?: "")
        val snowData = JSONObject(response.body?.string() ?: "")
        // Choose result format, 'metric' or 'imperial'
        //val snowData = jsonData.getJSONObject("metric")

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
    ////////////////////////////Recommendation///////////////////////////////////////////////////


}