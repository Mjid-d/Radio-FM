package com.radiomaroc.data

data class WeatherData(
    val cityName: String,
    val temperature: Int,
    val weatherDescription: String,
    val weatherEmoji: String,
    val currentTime: String
)
