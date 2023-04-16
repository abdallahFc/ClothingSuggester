package com.example.clothingsuggester.data

import WeatherData
import android.content.Context
import com.example.clothingsuggester.data.entities.Clothes

class ClothingHelper(private val context: Context) {

    fun suggestClothes(weatherData: WeatherData): List<Clothes> {
        val lastSuggestionWearTopId =
            loadLastSuggestionIdFromSharedPreferences(LAST_TOP_SUGGESTION_KEY)
        val lastSuggestionBottomWearId =
            loadLastSuggestionIdFromSharedPreferences(LAST_BOTTOM_SUGGESTION_KEY)
        val temp = weatherData.main.temp - 273
        val clothesList = filterClothesListByWeather(temp)
        val filteredClothesList =
            filterClothesListByLastSuggestion(
                lastSuggestionWearTopId,
                lastSuggestionBottomWearId,
                clothesList
            )
        filteredClothesList.apply {
            saveSuggestionIdToSharedPreferences(this.first().id, LAST_TOP_SUGGESTION_KEY)
            saveSuggestionIdToSharedPreferences(this.last().id, LAST_BOTTOM_SUGGESTION_KEY)
        }
        return getPairsOfClothes(filteredClothesList)
    }

    private fun getPairsOfClothes(clothesList: List<Clothes>): List<Clothes> {
        val topClothes = clothesList.filter { it.articleType == "topwear" }
        val bottomClothes = clothesList.filter { it.articleType == "bottomwear" }
        return listOf(topClothes.random(), bottomClothes.random())
    }

    private fun getWeatherSeason(season: String): List<Clothes> {
        return clothesList.filter { it.season == season }
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


    companion object {
        private const val SHARED_PREFS_NAME = "clothing_suggestions"
        private const val LAST_TOP_SUGGESTION_KEY = "last_top_suggestion"
        private const val LAST_BOTTOM_SUGGESTION_KEY = "last_bottom_suggestion"
        private val clothesList = listOf(
            Clothes(
                1,
                "topwear",
                "Winter",
                "Jeackt",
                "https://assets.ajio.com/medias/sys_master/root/20220120/GaVT/61e9837faeb2695cdd226529/-286Wx359H-441110840-darkblue-MODEL.jpg",
            ),
            Clothes(
                2,
                "topwear",
                "Winter",
                "Jeackt",
                "https://assets.ajio.com/medias/sys_master/root/20201225/uTHR/5fe610a2aeb2694fd3000aa6/-286Wx359H-441110840-jetblack-MODEL.jpg",
            ),
            Clothes(
                3,
                "topwear",
                "Winter",
                "Jeackt",
                "https://assets.ajio.com/medias/sys_master/root/20210615/K0Ca/60c7b263aeb269a9e3e7824d/-286Wx359H-441120416-black-MODEL.jpg",
            ),
            Clothes(
                4,
                "topwear",
                "Winter",
                "Jeackt",
                "https://assets.ajio.com/medias/sys_master/root/20210723/bGSh/60f9ca07aeb269a9e34e698f/-286Wx359H-410294630-4mk-MODEL.jpg",
            ),
            Clothes(
                5,
                "topwear",
                "Winter",
                "Jeackt",
                "https://assets.ajio.com/medias/sys_master/root/20211125/QkLK/619fb21caeb2690110d868c3/-286Wx359H-441124391-black-MODEL.jpg",
            ),
            Clothes(
                6,
                "bottomwear",
                "Winter",
                "Jeans",
                "https://assets.ajio.com/medias/sys_master/root/20210728/I1Im/61005afff997ddb3123c0416/-286Wx359H-441128531-jetblack-MODEL.jpg"
            ),
            Clothes(
                7,
                "bottomwear",
                "Winter",
                "Jeans",
                "https://assets.ajio.com/medias/sys_master/root/ha6/he3/15636645478430/-286Wx359H-441038489-black-MODEL.jpg"
            ),
            Clothes(
                8,
                "bottomwear",
                "Winter",
                "Jeans",
                "https://assets.ajio.com/medias/sys_master/root/h84/ha1/14876749266974/-286Wx359H-441038479-mediumblue-MODEL.jpg"
            ),
            Clothes(
                9,
                "bottomwear",
                "Summer",
                "Jeans",
                "https://assets.ajio.com/medias/sys_master/root/h67/h78/15636702035998/-286Wx359H-441038485-black-MODEL.jpg"
            ),
            Clothes(
                10,
                "bottomwear",
                "Summer",
                "Jeans",
                "https://assets.ajio.com/medias/sys_master/root/20220216/pOV4/620bfa1bf997dd03e2d1ea1d/-286Wx359H-441126173-medgrey-MODEL.jpg"
            ),
            Clothes(
                11,
                "bottomwear",
                "Summer",
                "sweat pants",
                "https://assets.ajio.com/medias/sys_master/root/20210205/kiCO/601c48ebaeb26969815dd70b/-286Wx359H-460836057-grey-MODEL.jpg"
            ),
            Clothes(
                12,
                "bottomwear",
                "Summer",
                "sweat pants",
                "https://assets.ajio.com/medias/sys_master/root/20210205/4HW3/601c40c9f997dd5c40e7b7b4/-286Wx359H-460836056-black-MODEL.jpg"
            ),
            Clothes(
                13,
                "bottomwear",
                "Spring",
                "Jeans",
                "https://assets.ajio.com/medias/sys_master/root/20210706/7vFe/60e36988aeb269a9e3381f95/-286Wx359H-441124255-mediumblue-MODEL.jpg"
            ),
            Clothes(
                14,
                "bottomwear",
                "Spring",
                "Jeans",
                "https://assets.ajio.com/medias/sys_master/root/20210706/7vFe/60e36988aeb269a9e3381f95/-286Wx359H-441124255-mediumblue-MODEL.jpg"
            ),
            Clothes(
                15,
                "bottomwear",
                "Spring",
                "sweat pants",
                "https://assets.ajio.com/medias/sys_master/root/20220103/a54g/61d33b35aeb2695cdd034fe5/-286Wx359H-463599588-grey-MODEL.jpg"
            ),
            Clothes(
                16,
                "bottomwear",
                "Fall",
                "sweat pants",
                "https://assets.ajio.com/medias/sys_master/root/20211104/V1m2/61831a5caeb2690110b97aae/-286Wx359H-460855739-blue-MODEL.jpg"
            ),
            Clothes(
                17,
                "bottomwear",
                "Fall",
                "Jeans",
                "https://assets.ajio.com/medias/sys_master/root/20211013/stqg/61661142f997ddf8f1cc10df/-286Wx359H-461192936-blue-MODEL.jpg"
            ),
            Clothes(
                18,
                "bottomwear",
                "Fall",
                "Jeans",
                "https://assets.ajio.com/medias/sys_master/root/20211006/9z0W/615d9a8ef997ddce89036c92/-286Wx359H-460977757-black-MODEL.jpg"
            ),
            Clothes(
                19,
                "topwear",
                "Summer",
                "shirt",
                "https://assets.ajio.com/medias/sys_master/root/20210818/gN8z/611c4954f997ddce89a30e9b/-286Wx359H-462811954-maroon-MODEL.jpg"
            ),
            Clothes(
                20,
                "topwear",
                "Summer",
                "shirt",
                "https://assets.ajio.com/medias/sys_master/root/20201110/qYpI/5fa9ad51f997dd8c8390f1bc/-286Wx359H-461573155-blue-MODEL.jpg"
            ),
            Clothes(
                21,
                "topwear",
                "Fall",
                "shirt",
                "https://assets.ajio.com/medias/sys_master/root/20210311/Ux3C/6049c78faeb26969817fa039/-286Wx359H-460849653-black-MODEL.jpg"
            ),
            Clothes(
                22,
                "topwear",
                "Fall",
                "shirt",
                "https://assets.ajio.com/medias/sys_master/root/20210814/3mkZ/6116d11baeb269a26872554b/-286Wx359H-460953860-black-MODEL.jpg"
            ),
            Clothes(
                23,
                "topwear",
                "Spring",
                "shirt",
                "https://assets.ajio.com/medias/sys_master/root/20210517/vvBv/60a265a2aeb269a9e3c1032b/-286Wx359H-461354922-yellow-MODEL.jpg"
            ),
            Clothes(
                24,
                "topwear",
                "Spring",
                "shirt",
                "https://assets.ajio.com/medias/sys_master/root/20211213/Jurp/61b741a9aeb2690110092549/-286Wx359H-469077652-white-MODEL.jpg"
            )


        )
    }

}
