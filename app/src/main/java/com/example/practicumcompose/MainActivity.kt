package com.example.practicumcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.ListItem
import com.example.practicumcompose.weather_lesson.WeatherApiScreen

private val CITY_NAME_FOR_WEATHER_API = "Izhevsk"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherApiScreen(
                cityName = CITY_NAME_FOR_WEATHER_API
            )


        }
    }

}

