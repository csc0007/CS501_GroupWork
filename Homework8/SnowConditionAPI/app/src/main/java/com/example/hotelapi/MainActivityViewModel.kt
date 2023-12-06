package com.example.hotelapi

import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivityViewModel : ViewModel() {

    private val client = OkHttpClient()

    val snowConditionLiveData = MutableLiveData<SnowCondition>()
    val errorLiveData = MutableLiveData<String>()

    data class SnowCondition(
        val topSnowDepth: String,
        val botSnowDepth: String,
        val freshSnowfall: String,
        val lastSnowfallDate: String
    )

    fun fetchSnowCondition(resortName: String) {
        viewModelScope.launch {
            try {
                val snowCondition = getSnowCondition(resortName)
                snowConditionLiveData.postValue(snowCondition)
            } catch (e: Exception) {
                errorLiveData.postValue(e.message)
            }
        }
    }

    private suspend fun getSnowCondition(resortName: String): SnowCondition = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("https://ski-resort-forecast.p.rapidapi.com/$resortName/snowConditions")
            .addHeader("X-RapidAPI-Key", "API-KEY-HERE")
            .addHeader("X-RapidAPI-Host", "ski-resort-forecast.p.rapidapi.com")
            .build()

        val response = client.newCall(request).execute()
        val jsonData = JSONObject(response.body?.string() ?: "")

        // Choose 'metric' for result, 'imperial' is also an option
        val snowData = jsonData.getJSONObject("metric")

        return@withContext SnowCondition(
            topSnowDepth = snowData.getString("topSnowDepth"),
            botSnowDepth = snowData.getString("botSnowDepth"),
            freshSnowfall = snowData.getString("freshSnowfall"),
            lastSnowfallDate = snowData.getString("lastSnowfallDate")
        )
    }
}
