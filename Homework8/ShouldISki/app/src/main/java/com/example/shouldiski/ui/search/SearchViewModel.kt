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

class SearchViewModel : ViewModel() {

    private val client = OkHttpClient()

    val roomAvailabilityData = MutableLiveData<String>()



    // LiveData to hold the text and date data
    private val _data = MutableLiveData<Pair<String, LocalDate?>>()
    val data: LiveData<Pair<String, LocalDate?>> = _data

    // Function to be called when data is submitted from the UI
    fun submitData(destination: String, date: LocalDate?) {

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun compareRoomAvailability(checkin: String, checkout: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val originalCount = getUniqueRoomCount(checkin, checkout)
                val sixMonthsLaterCount = getUniqueRoomCount(addSixMonths(checkin), addSixMonths(checkout))

                val percentage = ((sixMonthsLaterCount - originalCount).toFloat() / sixMonthsLaterCount.toFloat()) * 100
                withContext(Dispatchers.Main) {
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

    private suspend fun getUniqueRoomCount(checkin: String, checkout: String): Int {
        val request = Request.Builder()
            .url("https://booking-com-api3.p.rapidapi.com/booking/blockAvailability?checkin=" +
                    "$checkin&checkout=$checkout&is_24hr=0&show_test=0&show_only_test=0&units=metric&allow_past=0&user_platform" +
                    "=desktop&guest_qty=1&language=en&hotel_ids=191981&https_photos=0&detail_level=0&guest_cc=us")
            .addHeader("Accept", "application/json")
            .addHeader("X-RapidAPI-Key", "PUT-API-KEY-HERE")
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
        return parsedDate.plusMonths(6).format(DateTimeFormatter.ISO_LOCAL_DATE)
    }

}