package com.example.practicumcompose.base_anim_components

import android.graphics.ColorMatrixColorFilter
import android.graphics.Shader
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun FromOneToThreeFab() {
    val isMenuExtended = remember { mutableStateOf(false) }

    val fabAnim by animateFloatAsState(
        targetValue = if (isMenuExtended.value) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "fabAnim"
    )

    val renderEffect = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        getRenderEffect().asComposeRenderEffect()
    } else null

    FinalContainer(
        renderEffect = renderEffect,
        fabAnimationProgress = fabAnim,
        toggleAnimation = { isMenuExtended.value = !isMenuExtended.value }
    )
}

@RequiresApi(Build.VERSION_CODES.S)
private fun getRenderEffect(): android.graphics.RenderEffect {
    val blurEffect = android.graphics.RenderEffect
        .createBlurEffect(40f, 40f, Shader.TileMode.MIRROR)

    val alphaMatrix = android.graphics.RenderEffect.createColorFilterEffect(
        ColorMatrixColorFilter(
            floatArrayOf(
                1f, 0f, 0f, 0f, 0f,   // R
                0f, 1f, 0f, 0f, 0f,   // G
                0f, 0f, 1f, 0f, 0f,   // B
                0f, 0f, 0f, 20f, -255f * 10 // A ← вот исправление
            )
        )
    )

    return android.graphics.RenderEffect.createChainEffect(alphaMatrix, blurEffect)
}

@Composable
fun FinalContainer(
    renderEffect: RenderEffect?,
    fabAnimationProgress: Float = 0f,
    toggleAnimation: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 25.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        // 1. блюр слой на тёмном фоне — тут происходит gooey
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black), // ← тёмный фон обязателен
            contentAlignment = Alignment.BottomCenter
        ) {
            AnimFabGroup(
                renderEffect = renderEffect,
                animationProgress = fabAnimationProgress
            )
        }

        // 2. реальные кнопки поверх — клики работают
        AnimFabGroup(
            renderEffect = null,
            animationProgress = fabAnimationProgress,
            toggleAnimation = toggleAnimation
        )
    }
}


@Composable
fun AnimFabGroup(
    modifier: Modifier = Modifier,
    animationProgress: Float = 0f,
    renderEffect: RenderEffect? = null,
    toggleAnimation: () -> Unit = {}
) {

    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 8.dp)
            .graphicsLayer { this.renderEffect = renderEffect },
        contentAlignment = Alignment.BottomCenter
    ) {
        // кнопка вправо-вниз
        val progress1 = FastOutSlowInEasing.transform(0.2f, 0.7f, animationProgress)
        AnimFab(
            icon = Icons.Default.Build,
            modifier = Modifier.padding(
                bottom = (72.dp * progress1),
                end = (210.dp * progress1)
            ),
            opacity = LinearEasing.transform(0.2f, 0.7f, animationProgress),
            onClick = { Toast.makeText(context, "Кнопка 1 нажата", Toast.LENGTH_SHORT).show() }
        )

        // кнопка вверх
        val progress2 = FastOutSlowInEasing.transform(0.1f, 0.9f, animationProgress)
        AnimFab(
            icon = Icons.Default.Face,
            modifier = Modifier.padding(
                bottom = (88.dp * progress2)
            ),
            opacity = LinearEasing.transform(0.3f, 0.8f, animationProgress),
            onClick = { Toast.makeText(context, "Кнопка 2 нажата", Toast.LENGTH_SHORT).show() }
        )

        // кнопка влево-вниз
        val progress3 = FastOutSlowInEasing.transform(0.1f, 0.9f, animationProgress)
        AnimFab(
            icon = Icons.Default.Settings,
            modifier = Modifier.padding(
                bottom = (72.dp * progress3),
                start = (210.dp * progress3)
            ),
            opacity = LinearEasing.transform(0.4f, 0.9f, animationProgress),
            onClick = { Toast.makeText(context, "Кнопка 3 нажата", Toast.LENGTH_SHORT).show() }
        )

        // главная кнопка — поверх всех, крутится
        AnimFab(
            icon = Icons.Default.Add,
            modifier = Modifier.rotate(
                225f * FastOutSlowInEasing.transform(
                    from = 0.35f,
                    to = 0.65f,
                    fraction = animationProgress
                )
            ),
            onClick = toggleAnimation
        )
    }
}

fun Easing.transform(from: Float, to: Float, fraction: Float): Float {
    return transform(((fraction - from) / (to - from)).coerceIn(0f, 1f))
}

@Composable
fun AnimFab(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    opacity: Float = 1f,
    backgroundColor: Color = Color.Blue,
    onClick: () -> Unit = {}
) {
    SmallFloatingActionButton(
        onClick = { onClick() },
        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
        containerColor = backgroundColor,
        modifier = modifier.scale(1.25f)
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                tint = Color.White.copy(alpha = opacity)
            )
        }
    }
}