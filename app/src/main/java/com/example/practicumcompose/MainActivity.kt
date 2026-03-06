package com.example.practicumcompose

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListItemsLesson()
        }
    }

    @Composable
    private fun SayHello() {

        val context = LocalContext.current

        var text by remember { mutableStateOf("1") }
        val lastLineCount = remember { mutableIntStateOf(0) }

        LaunchedEffect(Unit) {
            delay(5000)
            text = "3"
        }

        Text(
            text = text,
            modifier = Modifier
                .padding(16.dp)
                .background(
                    color = Color(0xFFE1F5FE),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
                .clickable {
                    context.showToast("Клик сделан")
                },

            color = Color(0xFF01579B), // цвет текста
            fontSize = 27.sp,

//          //  (стиль: курсив или нормальный)
//          fontStyle = FontStyle.Italic,    // курсив
//          // жирность
//          fontWeight = FontWeight.Bold,    // жирный (можно Medium, Light, и т.д.)
//          // семейство шрифтов
//          fontFamily = FontFamily.SansSerif, // или Serif, Monospace
//          // подчеркивание, зачеркивание
//          textDecoration = TextDecoration.Underline, // подчеркнутый

            letterSpacing = 16.sp,
            textAlign = TextAlign.Center, // выравниваем ТОЛЬКО по горизонтали
            lineHeight = 32.sp, // расстояние между строками
            maxLines = 3,
            overflow = TextOverflow.Ellipsis, // что делать если текст не влезает

            // коллбэк после отрисовки текста
            onTextLayout = { textLayoutResult ->
                val lineCount: Int = textLayoutResult.lineCount
                if (lastLineCount.intValue != lineCount) {
                    lastLineCount.intValue = lineCount
                    context.showToast("Lines number = ${lineCount}")
                }
            },

            style = TextStyle(
                shadow = Shadow(
                    color = Color.Red,
                    offset = Offset(4f, 7f),
                    blurRadius = 8f
                )
            )
        )
    }
    fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }

    @Composable
    private fun PositionLesson() {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .systemBarsPadding()
                .fillMaxSize()
                .background(
                    color = Color(0xFFF696FF)
                )
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .background(
                        color = Color(0xFFFF9C2B)
                    )
                    .fillMaxWidth(0.7f)
            ) {
                SayHello()
                SayHello()
                SayHello()
            }

            Row() {
                SayHello()
                SayHello()
                SayHello()
            }

            Row() {
                SayHello()
                SayHello()
                SayHello()
            }
        }
    }

    @Composable
    private fun ListItems(firstName: String, profession: String) {

        val infiniteTransition = rememberInfiniteTransition()
        val borderWidth by infiniteTransition.animateFloat(
            initialValue = 2f, targetValue = 6f, animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse)
        )

        var offsetX by remember { mutableStateOf(0f) }

        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()
        val scale by animateFloatAsState(if (isPressed) 0.85f else 1f)

        val interactionSource2 = remember { MutableInteractionSource() }
        val isPressed2 by interactionSource2.collectIsPressedAsState() // ← было interactionSource, должно быть interactionSource2
        val scale2 by animateFloatAsState(
            if (isPressed2) 1.15f else 1f,
            animationSpec = spring(dampingRatio = 0.3f, stiffness = 400f)
        )

        val interactionSource3 = remember { MutableInteractionSource() } // ← для ripple картинки тоже свой

        val radius by infiniteTransition.animateFloat(
            initialValue = 40f, targetValue = 55f,
            animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse)
        )

        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            shape = RoundedCornerShape(40.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Green),
                contentAlignment = Alignment.BottomEnd
            ) {
                Column(
                    modifier = Modifier.background(Color.Red)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.background(Color.Yellow)

                    ) {
                        Image(
                            // пульсация
                            contentScale = ContentScale.Crop,
                            painter = painterResource(id = R.drawable.image),
                            contentDescription = "не понел",
                            modifier = Modifier
                                .size(120.dp)
                                .border(borderWidth.dp, Color.Cyan, CircleShape)
                                .clip(CircleShape)
                                .shadow(18.dp)
                                .background(color = Color(0xFFFF9C2B))
                        )
                        Column(
                            modifier = Modifier.background(Color.Blue)
                        ) {
                            Text(text = firstName)
                            Text(text = profession)
                        }

                    }


                    //можем двигать картинку внутри
                    Image(
                        painter = painterResource(id = R.drawable.image),
                        contentDescription = "не понел",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .pointerInput(Unit) {
                                detectDragGestures { _, drag -> offsetX += drag.x }
                            }
                            .graphicsLayer { translationX = offsetX * 0.3f }

                    )

                    // эффект длительного нажатия
                    Image(
                        painter = painterResource(id = R.drawable.image),
                        contentDescription = "не понел",
                        modifier = Modifier
                            .size(80.dp)
                            .scale(scale)
                            .clip(CircleShape)
                            .clickable(interactionSource, null) {}

                    )

                    // рипл-эффект
                    Image(
                        painter = painterResource(id = R.drawable.image),
                        contentDescription = "не понел",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .clickable(
                                interactionSource = interactionSource3,
                                indication = ripple(
                                    color = Color.Cyan,
                                    radius = 44.dp,
                                    bounded = false
                                )
                            ) {}
                    )

                    // приятный эффект пружинки
                    Image(
                        painter = painterResource(id = R.drawable.image),
                        contentDescription = "не понел",
                        modifier = Modifier
                            .size(80.dp)
                            .scale(scale2)
                            .clip(CircleShape)
                            .clickable(interactionSource2, null) {}
                    )

                    // Мерцание всего фона
                    Image(
                        painter = painterResource(id = R.drawable.image),
                        contentDescription = "не понел",
                        modifier = Modifier
                            .size(80.dp)
                            .drawBehind {
                                drawCircle(Color.Cyan.copy(alpha = 0.2f), radius = radius.dp.toPx())
                                drawCircle(
                                    Color.Red.copy(alpha = 0.1f),
                                    radius = (radius + 8f).dp.toPx()
                                )
                            }
                            .clip(CircleShape)
                    )
                }

            }

        }
    }
    
    @Composable
    private fun ModifierLesson() {
        val context = LocalContext.current

        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {


            Box(
                modifier = Modifier
                    .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                    .size(100.dp)
                    .background(Color.Red)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                        }
                    }
            )

            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .combinedClickable(
                        onLongClick = { context.showToast("Long click") },
                        onDoubleClick = { context.showToast("Double click") },
                        onClick = { context.showToast("click") }
                    )
            ) {
                repeat(15) {
                    Text(text = "Reapeted Text")
                }
            }
        }
    }

    @Composable
    private fun StateLesson() {
        val counter = remember { mutableStateOf(0) }

        val backgroundColor = remember { mutableStateOf(Color.Gray) }

        Text(
            modifier = Modifier
                .padding(44.dp)
                .clickable {
                    when (++counter.value) {
                        10 -> backgroundColor.value = Color.Red
                        20 -> backgroundColor.value = Color.Green
                    }
                }
                .background(color = backgroundColor.value),
            text = counter.value.toString()
        )
    }

    @Composable
    private fun ListItemsLesson() {
        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {

            itemsIndexed(
                listOf<String>("item 1", "item2", "item 923")
            ) { index, item ->
                Text(
                    text = "$item $index",
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }


        }
    }
}

