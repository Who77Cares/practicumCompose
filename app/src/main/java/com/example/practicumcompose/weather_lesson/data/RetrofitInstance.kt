package com.example.practicumcompose.weather_lesson.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object RetrofitInstance {

    private const val BASE_URL = "https://api.weatherapi.com/v1/"
    private const val API_KEY = "adb9e821267249a9832123033260803"

    // 1. Приватный Retrofit instance
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // 2. Публичный API интерфейс (такая реализация позволяет использовать retrofit для других апи, например astronomy.api
    private val weatherApi: WeatherApi by lazy {
        retrofit.create(WeatherApi::class.java)
    }

    suspend fun getWeatherApi(city: String): Double? {
        return withContext(Dispatchers.IO) {
            try {
                val response = weatherApi.getCurrentWeather(
                    apiKey = API_KEY,
                    location = city
                ).execute()

                if (response.isSuccessful) {
                    Log.d("--", "${response.body()?.current?.condition?.icon}")
                    response.body()?.current?.temp_c
                } else {
                    null
                }
            } catch (e: IOException) {
                null
            }
        }
    }
}