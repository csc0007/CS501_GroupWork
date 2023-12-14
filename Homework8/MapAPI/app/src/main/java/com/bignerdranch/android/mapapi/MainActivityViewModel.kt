package com.bignerdranch.android.mapapi

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
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

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
                    .addHeader("X-RapidAPI-Key", "e0fb2d06ebmsh13b4199c11aa6c3p1d57a3jsn2277e5c7358d")
                    .addHeader("X-RapidAPI-Host", "driving-directions1.p.rapidapi.com")
                    .build()
                val client = OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build()
                val response = client.newCall(request).execute()
                /*// Log the response for debugging
                val responseBody = response.body?.string() ?: ""
                Log.d("API Response", responseBody)*/
                val jsonData = JSONObject(response.body?.string())
                // Check if the "data" field is present in the JSON response
                if (jsonData.getString("status") == "OK" && jsonData.has("data")) {
                    val data = jsonData.getJSONObject("data")

                    if (data.has("best_routes")) {
                        val bestRoutes = data.getJSONArray("best_routes")
                        val firstRoute = bestRoutes.getJSONObject(0)

                        // Extracting some basic information
                        val routeName = firstRoute.getString("route_name")
                        val distance = firstRoute.getString("distance_label")
                        val duration = firstRoute.getString("duration_label")

                        withContext(Dispatchers.Main) {
                            routeInfo.value = "Route: $routeName, Distance: $distance, Duration: $duration"
                        }
                    }
                } else {
                    // Handle the case where "data" field is not present
                    withContext(Dispatchers.Main) {
                        routeInfo.value = "No route information available"
                    }
                }
            } catch (e: SocketTimeoutException) {
                withContext(Dispatchers.Main) {
                    routeInfo.value = "Request timed out. Please try again."
                }
            }catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    routeInfo.value = "Error: ${e.message}"
                }
            }
        }
    }
}
