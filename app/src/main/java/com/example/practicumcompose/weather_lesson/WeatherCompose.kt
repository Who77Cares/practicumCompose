package com.example.practicumcompose.weather_lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practicumcompose.weather_lesson.data.RetrofitInstance
import kotlinx.coroutines.launch


@Composable
fun WeatherApiScreen(cityName: String) {

    val state = remember { mutableDoubleStateOf(0.0) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .fillMaxWidth()
                .background(Color.Red),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = " Temp in $cityName = ${state.doubleValue} °C",
                fontSize = 30.sp
            )
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(Color.Green)
                .padding(vertical = 30.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = {
                    scope.launch {
                        state.doubleValue = RetrofitInstance.getWeatherApi(city = cityName) ?: 0.0
                    }
                }
            ) {
                Text(text = "Refresh", modifier = Modifier.padding(10.dp))
            }
        }
    }
}

