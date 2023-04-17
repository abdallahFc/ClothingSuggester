package com.example.clothingsuggester.data

import WeatherData
import android.content.Context
import android.widget.Toast
import com.example.clothingsuggester.data.entities.Clothes
import java.util.*

class ClothingHelper(private val context: Context) {

    fun suggestClothes(weatherData: WeatherData): List<Clothes> {
        val lastTopSuggestionId = loadLastSuggestionIdFromSharedPreferences(LAST_TOP_SUGGESTION_KEY)
        val lastBottomSuggestionId = loadLastSuggestionIdFromSharedPreferences(LAST_BOTTOM_SUGGESTION_KEY)
        val temp = weatherData.main.temp - 273
        val clothesList = filterClothesListByWeather(temp)
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val savedDay = loadCurrentDayFromSharedPreferences()
        if (currentDay == savedDay) {
            val top = DataManager.getClothesById(lastTopSuggestionId)
            val bottom = DataManager.getClothesById(lastBottomSuggestionId)
            if (top != null && bottom != null) {
                return getPairsOfClothes(listOf(top, bottom))
            }
        }

        val filteredClothesList = filterClothesListByLastSuggestion(
            lastTopSuggestionId,
            lastBottomSuggestionId,
            clothesList
        )

        filteredClothesList.firstOrNull()?.id?.let { topId ->
            saveSuggestionIdToSharedPreferences(topId, LAST_TOP_SUGGESTION_KEY)
        }
        filteredClothesList.lastOrNull()?.id?.let { bottomId ->
            saveSuggestionIdToSharedPreferences(bottomId, LAST_BOTTOM_SUGGESTION_KEY)
        }

        saveCurrentDayToSharedPreferences(currentDay)

        return getPairsOfClothes(filteredClothesList)
    }


    private fun getPairsOfClothes(clothesList: List<Clothes>): List<Clothes> {
        val topClothes = clothesList.filter { it.articleType == "topwear" }
        val bottomClothes = clothesList.filter { it.articleType == "bottomwear" }
        return listOf(topClothes.random(), bottomClothes.random())
    }

    private fun getWeatherSeason(season: String): List<Clothes> {
        return DataManager.getClothes().filter { it.season == season }
    }

    private fun loadLastSuggestionIdFromSharedPreferences(key: String): Int {
        val sharedPreferences =
            context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(key, -1)
    }

    private fun filterClothesListByWeather(temp: Double): List<Clothes> {
        return when (temp) {
            in 0.0..10.0 -> getWeatherSeason("Winter")
            in 10.0..20.0 -> getWeatherSeason("Fall")
            in 20.0..30.0 -> getWeatherSeason("Summer")
            in 30.0..40.0 -> getWeatherSeason("Spring")
            else -> emptyList()
        }
    }

    private fun filterClothesListByLastSuggestion(
        lastSuggestionId: Int,
        id: Int,
        clothesList: List<Clothes>
    ): List<Clothes> {
        return if (lastSuggestionId != -1 && id != -1) {
            clothesList.filter { it.id != lastSuggestionId && it.id != id }
        } else {
            clothesList
        }
    }

    private fun saveSuggestionIdToSharedPreferences(suggestionId: Int, key: String) {
        val sharedPreferences =
            context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt(key, suggestionId).apply()
    }
    private fun saveCurrentDayToSharedPreferences(day: Int) {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt(CURRENT_DAY_KEY, day).apply()
    }
    private fun loadCurrentDayFromSharedPreferences(): Int {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(CURRENT_DAY_KEY, -1)
    }



    companion object {
        private const val SHARED_PREFS_NAME = "clothing_suggestions"
        private const val LAST_TOP_SUGGESTION_KEY = "last_top_suggestion"
        private const val LAST_BOTTOM_SUGGESTION_KEY = "last_bottom_suggestion"
        private const val CURRENT_DAY_KEY = "current_day"

    }

}
