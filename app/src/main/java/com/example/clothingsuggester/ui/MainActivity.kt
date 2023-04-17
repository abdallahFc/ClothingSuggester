package com.example.clothingsuggester.ui

import WeatherData
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clothingsuggester.R
import com.example.clothingsuggester.data.ClothingHelper
import com.example.clothingsuggester.databinding.ActivityMainBinding
import com.example.clothingsuggester.network.WeatherApiService

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var clothesAdapter: ClothesAdapter
    private lateinit var clotherHelper: ClothingHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        setContentView(binding.root)
        showLoading()
        callWeatherApi(API_CITY)
    }

    private fun callWeatherApi(city: String) {
        val weatherApiService = WeatherApiService(API_SERVICE, this)
        weatherApiService.getWeatherData(city, { weatherData ->
            clotherHelper = ClothingHelper(this)
            val clothesSuggestion = clotherHelper.suggestClothes(weatherData)
            showImageForSeason(clothesSuggestion.first().season)
            clothesAdapter = ClothesAdapter(clothesSuggestion)
            binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            binding.recyclerView.adapter = clothesAdapter
            updateUi(weatherData)
            Log.d("TAG", "callWeatherApi: $clothesSuggestion")
            saveTopSuggestion(clothesSuggestion.first().id)
            saveBottomSuggestion(clothesSuggestion.last().id)
        }, {
            showError()
        })
    }

    private fun showLoading() {
        with(binding) {
            progressBar.visibility = View.VISIBLE
            imageWeather.visibility = View.GONE
            linearLayout.visibility = View.GONE
            textView9.visibility = View.GONE
            imgError.visibility = View.GONE
            cityName.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi(weatherData: WeatherData) {
        val temp = (weatherData.main.temp - 273).toInt()
        val maxTemp= (weatherData.main.temp_max - 273).toInt()
        val minTemp= (weatherData.main.temp_min - 273).toInt()
        with(binding) {
            cityName.text = "${weatherData.name}, ${weatherData.sys.country}"
            temperature.text = temp.toString()+"°"
            humidity.text = weatherData.main.humidity.toString()
            maxTemperature.text = "Max: "+maxTemp.toString()+"°"
            minTemperature.text = "Min: "+minTemp.toString()+"°"
            wind.text = weatherData.wind.speed.toString()
            pressure.text = weatherData.main.pressure.toString()
            stateWather.text = weatherData.weather[0].main
            progressBar.visibility = View.GONE
            imageWeather.visibility = View.VISIBLE
            linearLayout.visibility = View.VISIBLE
            textView9.visibility = View.VISIBLE
            imgError.visibility = View.GONE
            cityName.visibility = View.VISIBLE
        }
    }

    private fun showError() {
        with(binding) {
            linearLayout.visibility = View.GONE
            textView9.visibility = View.GONE
            imgError.visibility = View.VISIBLE
            cityName.visibility = View.GONE
            imageWeather.visibility = View.GONE
            progressBar.visibility = View.GONE
        }
    }
    fun showImageForSeason(season:String){
        when(season){
            "Winter"->{
                binding.imageWeather.setImageResource(R.drawable.winter)
            }
            "Summer"->{
                binding.imageWeather.setImageResource(R.drawable.sun)
            }
            "Spring"->{
                binding.imageWeather.setImageResource(R.drawable.spring)
            }
            "Fall"->{
                binding.imageWeather.setImageResource(R.drawable.fall)

            }
        }
    }
    private fun saveTopSuggestion(suggestionId: Int?) {
        sharedPreferences.edit().putInt(LAST_TOP_SUGGESTION_KEY, suggestionId ?: -1).apply()
    }
    private fun saveBottomSuggestion(suggestionId: Int?) {
        sharedPreferences.edit().putInt(LAST_BOTTOM_SUGGESTION_KEY, suggestionId ?: -1).apply()
    }


    companion object {
        private const val SHARED_PREFS_NAME = "clothing_suggestions"
        private const val LAST_TOP_SUGGESTION_KEY = "last_top_suggestion"
        private const val LAST_BOTTOM_SUGGESTION_KEY = "last_bottom_suggestion"
        private const val API_SERVICE = "06c921750b9a82d8f5d1294e1586276f"
        private const val API_CITY = "cairo"
    }

}
