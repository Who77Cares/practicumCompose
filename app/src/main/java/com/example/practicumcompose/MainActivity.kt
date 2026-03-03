package com.example.practicumcompose

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SayHello()
        }
    }

    @Composable
    private fun SayHello() {

        val context = LocalContext.current

        var text by remember { mutableStateOf("Hello") }
        val lastLineCount = remember { mutableIntStateOf(0) }

        LaunchedEffect(Unit) {
            delay(5000)
            text = "Very long text with many different lines and styles"
        }

        Text(
            text = text,
            modifier = Modifier
                .fillMaxSize() // растянуть на всю ширину
                .padding(16.dp)
                .systemBarsPadding() // 👈 ВАЖНО! Добавляет отступы для статус бара и навигации если ставим enableEdgeToEdge()
                .background(
                    color = Color(0xFFE1F5FE),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
//                .wrapContentSize(align = Alignment.Center)

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
}

