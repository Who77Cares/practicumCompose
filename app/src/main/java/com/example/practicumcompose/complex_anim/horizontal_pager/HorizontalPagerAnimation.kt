package com.example.practicumcompose.complex_anim.horizontal_pager

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun HorizontalPagerAnimation() {

    val listData: List<String> = listOf(
        "Первая страница",
        "Вторая страница",
        "Третья страница",
        "Четвертая страница",
        "Пятая страница",
        "Шестая страница",
    )


    val pagerState = rememberPagerState(
        pageCount = {
            listData.size
        }
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        TopScrollRow(
            data = listData,
            modifier = Modifier.fillMaxWidth(),
            pagerState = pagerState
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color(
                            Random.nextLong(0xFFFFFFFF)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = listData[page]
                )
            }
        }
    }

}

@Composable
fun TopScrollRow(
    data: List<String>,
    modifier: Modifier = Modifier,
    pagerState: PagerState // для клика
) {
    // для чего нужен?
    val listState = rememberLazyListState()

    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        if (data.isNotEmpty()) {
            listState.animateScrollToItem(
                index = pagerState.currentPage,
                scrollOffset = -100
            )
        }
    }

    LazyRow(
        state = listState,
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        itemsIndexed(data) { index, item ->

            val isSelected =
                pagerState.currentPage == index

            Card(
                modifier = Modifier
                    .size(35.dp)
                    .clickable {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                border =
                    if (isSelected) {
                        BorderStroke(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        null
                    },
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (index + 1).toString(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

}