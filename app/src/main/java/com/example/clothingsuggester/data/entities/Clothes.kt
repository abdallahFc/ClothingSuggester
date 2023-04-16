package com.example.clothingsuggester.data.entities

data class Clothes(
    val id: Int,
    val articleType: String,
    val season: String,
    val productDisplayName: String,
    val image: String
)