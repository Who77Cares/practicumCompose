package com.example.practicumcompose.base_anim_components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

data class QuestionModel(
    val id: Int,
    val questionText: String,
    val questionType: String,
    val tags: List<String>,
    val positionInTest: Int
)

@Composable
fun FabUI() {

    // скролл при листании списка вниз
    val listState = rememberLazyListState()
    var isScrollingDown by remember { mutableStateOf(false) }
    var previousIndex by remember { mutableIntStateOf(0) }
    var previousOffset by remember { mutableIntStateOf(0) }

    val questions = remember {
        List(50) { index ->
            QuestionModel(
                id = index,
                questionText = "Вопрос №${index + 1}: Это тестовый вопрос для проверки скролла?",
                questionType = "QuestionType.TEXT_INPUT MOKE!!!",
                tags = listOf("тег1", "тег2", "тег${index % 5}"),
                positionInTest = index + 1
            )
        }
    }


    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        }.collect { (index, offset) ->
            isScrollingDown = when {
                index > previousIndex -> true
                index < previousIndex -> false
                else -> offset > previousOffset
            }
            previousIndex = index
            previousOffset = offset
        }
    }

    val topBarVisible = !isScrollingDown || listState.firstVisibleItemIndex == 0


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .padding(bottom = 50.dp)
        ) {

            AnimatedVisibility(
                visible = topBarVisible,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Blue)
                            .padding(bottom = 15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {

                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = ""
                        )

                        Text(
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                            text = "точками = ---"
                        )

                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = ""
                        )

                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = ""
                        )

                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = ""
                        )
                    }

                }
            }

            LazyColumn(
                state = listState, // ⬅️ Тот же state для отслеживания скролла
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.3f))
            ) {
                itemsIndexed(
                    items = questions,
                    key = { _, question -> question.id } // ⬅️ Уникальный ключ для оптимизации
                ) { index, question ->
                    // 7. Карточка вопроса (упрощенная версия TaskItemForLazy)
                    ScrollComponentUI(
                        posInText = question.positionInTest.toString()
                    )
                }
            }
        }
    }

}

@Composable
fun ScrollComponentUI(
    posInText: String
){
    Box(modifier = Modifier.wrapContentSize()) {
        Column(

        ) {
            Box(
                modifier = Modifier
                    .size(height = 112.dp, width = 59.dp)
                    .background(Color.Cyan)
                    .padding(22.dp)
            ) {
                Text(text = posInText, Modifier.padding(12.dp).background(Color.White))

            }
        }
    }
}

