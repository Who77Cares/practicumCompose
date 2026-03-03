package com.example.practicumcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
        Text(
            text = "Hello",
            modifier = Modifier
                .padding(16.dp)
                .systemBarsPadding() // 👈 ВАЖНО! Добавляет отступы для статус бара и навигации оставляем  enableEdgeToEdge()

                .background(
                    color = Color(0xFFE1F5FE),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
        )
    }
}

