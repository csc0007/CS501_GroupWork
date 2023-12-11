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

class ShareViewModel : ViewModel() {

    private val client = OkHttpClient()

    val roomAvailabilityData = MutableLiveData<String>()
    val resortName = MutableLiveData<String>()
    val hotelPercentage = MutableLiveData<Double>()

    // LiveData to hold the text and date data
    private val _data = MutableLiveData<Pair<String, LocalDate?>>()
    val data: LiveData<Pair<String, LocalDate?>> = _data

    // Function to be called when data is submitted from the UI
    @RequiresApi(Build.VERSION_CODES.O)
    fun submitData(destination: String, date: LocalDate?) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val checkInDate = date?.format(formatter).toString()
        val nextday=date?.plusDays(1)       //set default hotel check for 1 day
        val checkOutDate = nextday?.format(formatter).toString()
        if (destination=="stowe")
        {
            resortName.value="Stowe Mountain Resort"
            //hotelID is hard coded here, there will be a database to save this information
            compareRoomAvailability(checkInDate, checkOutDate,"191981")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun compareRoomAvailability(checkin: String, checkout: String, hotelID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val originalCount = getUniqueRoomCount(checkin, checkout, hotelID)
                val sixMonthsLaterCount = getUniqueRoomCount(addSixMonths(checkin), addSixMonths(checkout), hotelID)
                // compare with availability after six month (assume this is the empty status)
                val hotelPercentage = ((sixMonthsLaterCount - originalCount).toFloat() / sixMonthsLaterCount.toFloat()) * 100
                withContext(Dispatchers.Main) {
                    roomAvailabilityData.value = "Available rooms: $originalCount\n" +
                            "Available rooms after six months: $sixMonthsLaterCount\n" +
                            "Percentage of rooms booked: ${"%.2f".format(hotelPercentage)}%"
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
            .addHeader("X-RapidAPI-Key", "054999d8c2mshb298a4b5c4ddea0p15eb49jsnabf9c5983234")
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
}