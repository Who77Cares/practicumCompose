package com.example.practicumcompose.weather_lesson

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.practicumcompose.R
import com.example.practicumcompose.weather_lesson.data.RetrofitInstance
import kotlinx.coroutines.launch


@Composable
fun WeatherApiScreen(cityName: String) {

    val apiTemp = remember { mutableDoubleStateOf(0.0) }
    val apiWeatherIcon = remember { mutableStateOf("") }

    // для отображения индикатора загрузки\картинки
    val isLoading = remember { mutableStateOf(false) }

    // это корутина (контейнер), которая выполняет задачу (suspend функцию) 
    // scope - это контейнер, в котором живут корутины
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

            Column(
                modifier = Modifier.background(Color.Cyan),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = " Temp in $cityName = ${apiTemp.doubleValue} °C",
                    fontSize = 30.sp
                )

                Box(contentAlignment = Alignment.Center) {
                    AsyncImage(
                        model = apiWeatherIcon.value,
                        contentDescription = "apiWeatherIcon",
                        modifier = Modifier
                            .size(64.dp)
                            .alpha(if (isLoading.value) 0f else 1f) // прячем визуально, но не убираем
                            .border(2.dp, Color.Blue, CircleShape),
                        error = painterResource(id = R.drawable.image),
                        placeholder = painterResource(id = R.drawable.image),
                        onLoading = { isLoading.value = true },
                        onSuccess = { isLoading.value = false },
                        onError = { isLoading.value = false }
                    )

                    if (isLoading.value) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            strokeWidth = 2.dp
                        )
                    }
                }
            }
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
                        val retrofitResponse =
                            RetrofitInstance.getWeatherApi(city = cityName)
                                ?: Pair<Double, String>(0.0, "")

                        apiTemp.doubleValue = retrofitResponse.first
                        apiWeatherIcon.value = "https:${retrofitResponse.second}"
                        Log.d("--", apiWeatherIcon.value)

                    }
                }
            ) {
                Text(text = "Refresh", modifier = Modifier.padding(10.dp))
            }
        }
    }
}

