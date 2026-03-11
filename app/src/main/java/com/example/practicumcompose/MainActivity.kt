package com.example.practicumcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.practicumcompose.weather_lesson.WeatherTabLayoutLesson

private const val CITY_NAME_FOR_WEATHER_API = "Izhevsk"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherTabLayoutLesson(CITY_NAME_FOR_WEATHER_API)
        }
    }

}

