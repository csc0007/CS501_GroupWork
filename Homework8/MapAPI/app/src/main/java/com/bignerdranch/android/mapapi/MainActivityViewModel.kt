package com.bignerdranch.android.mapapi

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
    val routeInfo = MutableLiveData<String>()

    fun getDrivingDirections(origin: String, destination: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val urlOrigin = origin.replace(" ", "%20")
                val urlDestination = destination.replace(" ", "%20")
                val request = Request.Builder()
                    .url("https://driving-directions1.p.rapidapi.com/get-directions?origin=$urlOrigin&destination=$urlDestination&avoid_routes=tolls,ferries&country=us&language=en")
                    .get()
                    .addHeader("X-RapidAPI-Key", "YOUR-API-KEY")
                    .addHeader("X-RapidAPI-Host", "driving-directions1.p.rapidapi.com")
                    .build()

                val response = client.newCall(request).execute()
                val jsonData = JSONObject(response.body?.string() ?: "")
                val route = jsonData.getJSONObject("data").getJSONArray("best_routes").getJSONObject(0)
                val distance = route.getString("distance_label")
                val duration = route.getString("duration_label")

                withContext(Dispatchers.Main) {
                    routeInfo.value = "Distance: $distance\nDuration: $duration"
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    routeInfo.value = "Error: ${e.message}"
                }
            }
        }
    }
}
