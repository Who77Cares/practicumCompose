package com.example.practicumcompose.base_anim_components.navigation_anim

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Green.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Splash Screen",
            fontSize = 25.sp
        )
    }
}

@Composable
fun MainScreen(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.White.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.clickable{
                onClick()
            },
            text = "Main Screen",
            fontSize = 25.sp
        )
    }
}

@Composable
fun SecondScreen() {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Red.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Second screen",
            fontSize = 25.sp
        )
    }
}