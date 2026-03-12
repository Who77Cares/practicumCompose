package com.example.practicumcompose.weather_lesson


import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.practicumcompose.R
import com.example.practicumcompose.showToast
import com.example.practicumcompose.ui_theme.Purple700
import com.example.practicumcompose.weather_lesson.data.RetrofitInstance
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun WeatherTabLayoutLesson() {

    val dialogIsShown = remember { mutableStateOf(false) }

    val cityName = remember { mutableStateOf("Izhevsk") }

    Column {
        WeatherApiScreen(
            cityName = cityName.value,
            onIconButtonClick = {
                dialogIsShown.value = true
            }
        )
        TabLayout()

        if (dialogIsShown.value) DialogScreen(
            dialogIsShown,
            getTextFieldValue = {
                cityName.value = it
            }
        )
    }
}


@Composable
fun WeatherApiScreen(cityName: String, onIconButtonClick: () -> Unit) {

    val apiTemp = remember { mutableDoubleStateOf(0.0) }
    val apiWeatherIcon = remember { mutableStateOf("") }

    // для отображения индикатора загрузки\картинки
    val isLoading = remember { mutableStateOf(false) }

    // это корутина (контейнер), которая выполняет задачу (suspend функцию)
    // scope - это контейнер, в котором живут корутины
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxHeight(0.33f)
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

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            val retrofitResponse =
                                RetrofitInstance.getWeatherApi(city = cityName)
                                    ?: Pair<Double, String>(0.0, "")

                            apiTemp.doubleValue = retrofitResponse.first
                            apiWeatherIcon.value = "https:${retrofitResponse.second}"
                        }
                    }
                ) {
                    Text(text = "Refresh", modifier = Modifier.padding(10.dp))
                }

                IconButton(
                    onClick = { onIconButtonClick.invoke() }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.favorite_icon),
                        contentDescription = "icon",
                        tint = Color.Red
                    )
                }

            }

        }
    }
}

@Composable
fun TabLayout() {

    val tabList = listOf("HOURS", "DAYS", "Years", "Song")
    val pagerState = rememberPagerState { tabList.size }
    val tabIndex = pagerState.currentPage

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(start = 5.dp, end = 5.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {
        TabRow(
//            containerColor = Color.Green, - если не используем изменение цветов в Tab Modifier.background, то цвет таба можно задать тут
            contentColor = Color.White,
            selectedTabIndex = tabIndex,
            indicator = { position ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(position[tabIndex]),
                    color = Purple700
                )
            },
        ) {
            tabList.forEachIndexed { index, value ->
                Tab(
                    selected = index == tabIndex, // для кастомизации выбранного таба
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(text = value)
                    },
                    modifier = Modifier
                        .background(
                            if (index == tabIndex) Color.Yellow else Color.Cyan
                        )
                    // закомиченный кусок кода ниже - для отрисовки вертикальной линии между табами
//                        .drawWithContent {
//                            drawContent()
//                            if (index < tabList.lastIndex) {
//                                drawLine(
//                                    color = Color.Black,
//                                    start = Offset(size.width, 0f),
//                                    end = Offset(size.width, size.height),
//                                    strokeWidth = 2.dp.toPx()
//                                )
//                            }
//                        }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1.0f)
        ) { index ->
            if (index == 0) Text(text = "First TAB") else Text(text = "Second TAB")
        }
    }
}


@Composable
fun DialogScreen(dialogIsShown: MutableState<Boolean>, getTextFieldValue: (String) -> Unit) {

    val dialogText = remember { mutableStateOf("") }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    // Сохраняем предыдущий таймер для отмены
    val searchJob = remember { mutableStateOf<Job?>(null) }

    AlertDialog(
        // Срабатывает, когда пользователь пытается закрыть диалог не через кнопки: Нажатие вне области диалога
        onDismissRequest = {
            dialogIsShown.value = false
        },
        confirmButton = {
            TextButton(
                onClick = {
                    getTextFieldValue(dialogText.value)
                    dialogIsShown.value = false
                }
            ) {
                Text("YES!")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { dialogIsShown.value = false }
            ) {
                Text("NOOO")
            }
        },
        title = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "This is my title")
                TextField(value = dialogText.value, onValueChange = { newValue ->
                    dialogText.value = newValue

                    // Отменяем предыдущий таймер
                    searchJob.value?.cancel()

                    searchJob.value = coroutineScope.launch {
                        delay(4000)
                        context.showToast(dialogText.value)
                    }
                })
            }
        }
    )
}




