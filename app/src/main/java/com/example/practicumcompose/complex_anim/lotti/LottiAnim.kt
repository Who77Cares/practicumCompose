package com.example.practicumcompose.complex_anim.lotti

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.practicumcompose.R

@Composable
fun LazyBox(
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier
        .height(300.dp)
        .width(300.dp)
        .background(Color.Gray.copy(alpha = 0.5f))
    ) {
        content()
    }
}
@Composable
fun AllLottiScreen() {


    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.stars)
    )

    val composition2 by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.a_lotti)
    )

    val composition3 by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.b_lotti)
    )

    val composition4 by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.c_lotti)
    )

    val composition5 by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.d_lotti)
    )




    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
           LazyBox { IterateForeverLotti(composition) }
        }

        item {
            LazyBox { OneIteration(composition2) }
        }

        item {
            LazyBox { ExactRange(composition3) }
        }

        item {
            LazyBox { ReversAnim(composition5) }
        }

        item {
            AnimateByClick(composition4)
        }
    }
}

@Composable
fun IterateForeverLotti(
    composition: LottieComposition?
) {
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
        speed = 0.4f
    )

}

// 1 раз и стоп
@Composable
fun OneIteration(
    composition: LottieComposition?
) {
    LottieAnimation(
        composition = composition,
        iterations = 1,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Fit,
        speed = 0.8f
    )
}

// Воспроизводить только кусок анимации (например с 50% до 75%
@Composable
fun ExactRange(
    composition: LottieComposition?
) {

    val progress by animateLottieCompositionAsState(
        composition = composition,
        clipSpec = LottieClipSpec.Progress(0.5f, 0.75f),
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        composition = composition,
        progress = { progress }
    )
}

// ping-pong анимация (reverseOnRepeat = true) + в обратном направлении (speed = -1f)
@Composable
fun ReversAnim(
    composition: LottieComposition?
) {
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        reverseOnRepeat = true,
        speed = -1f
    )
}

// воспроизведение при клике + modifier в самом lotti + пауза
// тут используется декларативный подход (более простой в реализации)
// если требуется более гибкое управление, то используй val progress by animateLottieCompositionAsState
@Composable
fun AnimateByClick(
    composition: LottieComposition?
) {

    var play by remember { mutableStateOf(false) }

    LottieAnimation(
        composition = composition,
        isPlaying = play,
        restartOnPlay = false,
        iterations = LottieConstants.IterateForever,
        modifier = Modifier
            .size(200.dp)
            .background(Color.Gray.copy(alpha = 0.5f))
            .clickable {
                play = !play
            }
    )
}

