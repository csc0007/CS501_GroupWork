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
    val forecastData = MutableLiveData<String>()

    fun fetchSkiResortForecast(resortName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = makeApiRequest(resortName)
                if (response.isNotEmpty()) {
                    Log.d("API_RESPONSE", response) // Log the raw response
                    val parsedData = parseBasicInfo(response)
                    withContext(Dispatchers.Main) {
                        forecastData.value = parsedData
                    }
                } else {
                    Log.e("API_CALL", "Response was empty")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    forecastData.value = "Error: ${e.message}"
                    Log.e("API_CALL", "Error: ${e.message}")
                }
            }
        }
    }

    private fun makeApiRequest(resortName: String): String {
        val request = Request.Builder()
            .url("https://ski-resort-forecast.p.rapidapi.com/$resortName/forecast?units=i&el=top")
            .get()
            .addHeader("X-RapidAPI-Key", "e0fb2d06ebmsh13b4199c11aa6c3p1d57a3jsn2277e5c7358d") // Replace with your API key
            .addHeader("X-RapidAPI-Host", "ski-resort-forecast.p.rapidapi.com")
            .build()
        val response = client.newCall(request).execute()
        return response.body?.string() ?: ""
    }

    private fun parseBasicInfo(jsonData: String): String {
        try {
            val jsonObject = JSONObject(jsonData)
            val basicInfo = jsonObject.getJSONObject("basicInfo")
            val region = basicInfo.getString("region")
            val name = basicInfo.getString("name")
            val url = basicInfo.getString("url")

            return "Region: $region\nName: $name\nURL: $url"
        } catch (e: Exception) {
            Log.e("JSON_PARSE", "Error parsing JSON: ${e.message}")
            return "Error parsing JSON: ${e.message}"
        }
    }
}