package com.example.clothingsuggester.network

import WeatherData
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class WeatherApiService(val apiKey: String, val context: Context) {

    fun getWeatherData(city: String, successCallback: (WeatherData) -> Unit, failureCallback: () -> Unit) {
        val url = buildUrl(city)
        val request = buildRequest(url)
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Handler(Looper.getMainLooper()).post {
                    showToast("Error: ${e.message}")
                    failureCallback()
                }
            }
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val weatherData = jsonParser(body)
                Handler(Looper.getMainLooper()).post {
                    successCallback(weatherData)
                }
            }
        })
    }

    private fun buildUrl(city: String): HttpUrl {
        return HttpUrl.Builder()
            .scheme("https")
            .host("api.openweathermap.org")
            .addPathSegment("data")
            .addPathSegment("2.5")
            .addPathSegment("weather")
            .addQueryParameter("q", city)
            .addQueryParameter("appid", apiKey)
            .build()
    }

    private fun buildRequest(url: HttpUrl): Request {
        return Request.Builder()
            .url(url)
            .build()
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun jsonParser(body: String?): WeatherData {
        return Gson().fromJson(body, WeatherData::class.java)
    }
}
