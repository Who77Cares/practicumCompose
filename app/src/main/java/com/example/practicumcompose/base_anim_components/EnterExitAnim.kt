package com.example.practicumcompose.base_anim_components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun EnterExitAnim() {

    val animNames = listOf(
        "SlideUp", "SlideLeft", "Zoom", "FlipX",
        "ElasticDrop", "BlurIn", "SplitReveal", "Enter", "Fade", "CascadeList"
    )
    val visibleStates = remember { mutableStateMapOf<String, Boolean>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Кнопки-триггеры
        animNames.forEach { name ->
            Button(
                onClick = {  visibleStates[name] = visibleStates[name] != true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Показать $name")
            }
        }


        // Все анимации
        SlideUpAnim(
            isVisible = visibleStates["SlideUp"] == true,
            onDismiss = { visibleStates["SlideUp"] = false }
        )


        SlideLeftAnim(
            isVisible = visibleStates["SlideLeft"] == true,
            onDismiss = { visibleStates["SlideLeft"] = false }
        )


        ZoomAnim(
            isVisible = visibleStates["Zoom"] == true,
            onDismiss = { visibleStates["Zoom"] = false }
        )
        FlipXAnim(
            isVisible = visibleStates["FlipX"] == true,
            onDismiss = { visibleStates["FlipX"] = false }
        )
        ElasticDropAnim(
            isVisible = visibleStates["ElasticDrop"] == true,
            onDismiss = { visibleStates["ElasticDrop"] = false }
        )
        BlurInAnim(
            isVisible = visibleStates["BlurIn"] == true,
            onDismiss = { visibleStates["BlurIn"] = false }
        )
        SplitRevealAnim(
            isVisible = visibleStates["SplitReveal"] == true,
            onDismiss = { visibleStates["SplitReveal"] = false }
        )
        EnterAnim(
            isVisible = visibleStates["Enter"] == true,
            onDismiss = { visibleStates["Enter"] = false }
        )


        FadeAnim(
            isVisible = visibleStates["Fade"] == true,
            onDismiss = { visibleStates["Fade"] = false }
        )

        CascadeList(
            visible = visibleStates["CascadeList"] == true,
            items = animNames,
        )
    }
}

@Composable
fun EnterAnim(
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally(tween(400)) { it } + expandVertically(tween(400)),
        exit = slideOutHorizontally(tween(400)) { it } + shrinkVertically(tween(400))    // уезжает вправо
    ) {
        Button(onClick = {
            onDismiss()
        }) {
            Text("EnterAnim")
        }
    }
}

@Composable
fun FadeAnim(
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(400)),
        exit = fadeOut(tween(400))
    ) {
        TextButton(onClick = { onDismiss }
        ) {
            Text("FadeAnim")
        }
    }
}

@Composable
fun SlideUpAnim(
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(350)) + slideInVertically(
            initialOffsetY = { it },                          // снизу
            animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow)
        ),
        exit = fadeOut(tween(300)) + slideOutVertically(
            targetOffsetY = { it }
        )
    ) {
        TextButton(onClick = { onDismiss() }
        ) {
            Text("SlideUpAnim")
        }
    }
}

@Composable
fun ZoomAnim(
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(300)) + scaleIn(
            initialScale = 0.3f,
            animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium)
        ),
        exit = fadeOut(tween(250)) + scaleOut(
            targetScale = 0.3f,
            animationSpec = tween(250, easing = FastOutLinearInEasing)
        )
    ) {
        TextButton(onClick = { onDismiss }
        ) {
            Text("ZoomAnim")
        }
    }
}

@Composable
fun SlideLeftAnim(
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(350)) + slideInHorizontally(
            initialOffsetX = { -it },
            animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow)
        ),
        exit = fadeOut(tween(280)) + slideOutHorizontally(
            targetOffsetX = { -it },
            animationSpec = tween(280, easing = FastOutLinearInEasing)
        )
    ) {
        TextButton(onClick = { onDismiss }
        ) {
            Text("SlideLeftAnim")
        }
    }
}

@Composable
fun FlipXAnim(
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(350)) + scaleIn(
            initialScale = 0.5f,
            animationSpec = spring(Spring.DampingRatioMediumBouncy)
        ) + slideInVertically(
            initialOffsetY = { -it / 2 },
            animationSpec = spring(Spring.DampingRatioMediumBouncy)
        ),
        exit = fadeOut(tween(280)) + scaleOut(
            targetScale = 0.5f,
            animationSpec = tween(280)
        ) + slideOutVertically(
            targetOffsetY = { -it / 2 },
            animationSpec = tween(280)
        )
    ) {
        TextButton(onClick = { onDismiss }
        ) {
            Text("FlipXAnim")
        }
    }
}

@Composable
fun ElasticDropAnim(
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(100)) + slideInVertically(
            initialOffsetY = { -it },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        ) + scaleIn(
            initialScale = 0.6f,
            animationSpec = spring(Spring.DampingRatioMediumBouncy)
        ),
        exit = fadeOut(tween(200)) + slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = tween(200, easing = FastOutLinearInEasing)
        )
    ) {
        TextButton(onClick = { onDismiss }
        ) {
            Text("ElasticDropAnim")
        }
    }
}

@Composable
fun BlurInAnim(isVisible: Boolean, onDismiss: () -> Unit) {
    val alpha = remember { Animatable(0f) }
    val blur = remember { Animatable(12f) }
    val scale = remember { Animatable(1.15f) }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            launch { alpha.animateTo(1f, tween(450)) }
            launch { blur.animateTo(0f, tween(450)) }
            scale.animateTo(1f, tween(450, easing = FastOutSlowInEasing))
        } else {
            launch { alpha.animateTo(0f, tween(350)) }
            launch { blur.animateTo(12f, tween(350)) }
            scale.animateTo(1.15f, tween(350))
        }
    }

    Box(
        modifier = Modifier
            .graphicsLayer(alpha = alpha.value, scaleX = scale.value, scaleY = scale.value)
            .blur(blur.value.dp)
    ) {
        TextButton(onClick = { onDismiss }
        ) {
            Text("ElasticDropAnim")
        }
    }
}


@Composable
fun SplitRevealAnim(isVisible: Boolean, onDismiss: () -> Unit) {
    Box(modifier = Modifier.clipToBounds()) {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(
                initialOffsetY = { it },      // приходит снизу внутри клипа
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            ),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(320, easing = FastOutLinearInEasing)
            )
        ) {
            TextButton(onClick = { onDismiss }
            ) {
                Text("SpirRevealAnim")
            }
        }
    }
}

@Composable
fun CascadeList(visible: Boolean, items: List<String>) {
    items.forEachIndexed { index, item ->
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(300, delayMillis = index * 80)) +
                    slideInVertically(
                        initialOffsetY = { it / 2 },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMediumLow,
                            visibilityThreshold = IntOffset.VisibilityThreshold // ✅
                        )
                    ) +
                    scaleIn(
                        initialScale = 0.8f,
                        animationSpec = tween(300, delayMillis = index * 80)
                    ),
            exit = fadeOut(
                tween(200, delayMillis = (items.size - index) * 50)
            ) +
                    slideOutVertically(
                        targetOffsetY = { it / 2 },
                        animationSpec = tween(
                            200, delayMillis = (items.size - index) * 50
                        )
                    )
        ) {
            Text(item)
        }
    }
}
