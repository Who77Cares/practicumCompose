package com.example.practicumcompose.weather_lesson.data

data class WeatherResponse(
    val current: CurrentWeather
)

data class CurrentWeather(
    val temp_c: Double,
    val condition: Condition
)

data class Condition(
    val icon: String
)