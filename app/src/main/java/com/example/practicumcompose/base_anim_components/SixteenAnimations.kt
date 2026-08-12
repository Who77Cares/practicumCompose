package com.example.practicumcompose.base_anim_components

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun SimpleIconForAnim(
    modifier: Modifier = Modifier
) {
    Icon(
        modifier = Modifier
            .size(40.dp)
            .then(modifier),
        imageVector = Icons.Default.Face,
        contentDescription = null,
        tint = Color.Unspecified
    )
}

@Composable
fun SixteenAnimations() {
    Row(){
        Column(modifier = Modifier.fillMaxHeight().verticalScroll(rememberScrollState())) {

            // 1
            Text("SpringBounceAnim")
            SpringBounceAnim()

            // 2
            Text("RotateBurstIcon")
            RotateBurstIcon()

            // 3
            Text("ShakeIcon")
            ShakeIcon()

            // 4
            Text("MorphFillIcon")
            MorphFillIcon()

            // 5
            Text("ElasticStretchIcon")
            ElasticStretchIcon()
        }

        Column(modifier = Modifier.fillMaxHeight().verticalScroll(rememberScrollState())) {

            // 6
            Text("FlyOutIcon")
            FlyOutIcon()

            // 7
            Text("BlobIcon")
            BlobIcon()

            // 8
            Text("RubberBandIcon")
            RubberBandIcon()

            // 9
            Text("OrbitBurstIcon")
            OrbitBurstIcon()

            // 10
            Text("GravityDropIcon")
            GravityDropIcon()

            // 11
            Text("GlitchIcon")
            GlitchIcon()

        }

        Column(modifier = Modifier.fillMaxHeight().verticalScroll(rememberScrollState())) {

            // 12
            Text("GlitchIcon")
            PendulumSwingIcon()

            // 13
            Text("SpiralInIcon")
            SpiralInIcon()

            // 14
            Text("SquishWallIcon")
            SquishWallIcon()

            // 15
            Text("HeartbeatIcon")
            HeartbeatIcon()

            // 16
            Text("WormholeIcon")
            WormholeIcon()
        }
    }
}

// 1. SPRING BOUNCE — иконка пружинисто сжимается
@SuppressLint("SuspiciousIndentation")
@Composable
fun SpringBounceAnim() {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 1.40f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )
    IconButton(
        modifier = Modifier
            .size(130.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale),
        onClick = {
            pressed = true
        }
    ) {

        LaunchedEffect(pressed) {
            if (pressed) {
                delay(110)
                pressed = false
            }
        }

        SimpleIconForAnim()
    }
}

// 2. ROTATE BURST — поворот на 180° с overshoot
@Composable
fun RotateBurstIcon() {
    var rotationKey by remember { mutableStateOf(0) }
    val rotation by animateFloatAsState(
        targetValue = (rotationKey * 180f),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        )
    )
    IconButton(
        modifier = Modifier.size(130.dp),
        onClick = { rotationKey++ }
    ) {

        SimpleIconForAnim(
            modifier = Modifier.graphicsLayer { rotationZ = rotation }
        )
    }
}

@Composable
fun ShakeIcon() {
    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    IconButton(
        onClick = {
            scope.launch {
                val keyframes = listOf(0f, -14f, 12f, -10f, 7f, -4f, 0f)
                val durations = listOf(60, 70, 70, 70, 80, 100)
                for (i in keyframes.indices) {
                    if (i == 0) continue
                    offsetX.animateTo(
                        targetValue = keyframes[i],
                        animationSpec = tween(
                            durationMillis = durations[i - 1],
                            easing = LinearEasing
                        )
                    )
                }
            }
        },
        modifier = Modifier
            .size(130.dp)
            .graphicsLayer(translationX = offsetX.value)
    ) {

        SimpleIconForAnim()

    }
}

@Composable
fun MorphFillIcon() {
    var active by remember { mutableStateOf(false) }
    val bgColor by animateColorAsState(
        targetValue = if (active) Color(0xFFEEEDFE) else Color.Transparent,
        animationSpec = tween(250)
    )
    val iconColor by animateColorAsState(
        targetValue = if (active) Color(0xFF534AB7) else LocalContentColor.current,
        animationSpec = tween(250)
    )
    val scale by animateFloatAsState(
        targetValue = if (active) 1.25f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )
    IconButton(
        onClick = { active = !active },
        modifier = Modifier
            .size(130.dp)
            .clip(CircleShape)
            .background(bgColor)
    ) {

        SimpleIconForAnim(
            modifier = Modifier.graphicsLayer { scaleX = scale; scaleY = scale }
        )

    }
}

@Composable
fun FlyOutIcon() {
    val offsetY = remember { Animatable(0f) }
    val alpha = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    IconButton(
        onClick = {
            scope.launch {
                launch {
                    offsetY.animateTo(-40f, tween(200, easing = FastOutLinearInEasing))
                    offsetY.snapTo(32f)
                    offsetY.animateTo(0f, tween(280, easing = FastOutSlowInEasing))
                }
                launch {
                    alpha.animateTo(0f, tween(180))
                    alpha.snapTo(0f)
                    delay(20)
                    alpha.animateTo(1f, tween(280))
                }
            }
        },
        modifier = Modifier
            .size(130.dp)
            .graphicsLayer(translationY = offsetY.value, alpha = alpha.value)
    ) {
        SimpleIconForAnim()
    }
}

// 2. BLOB — схлопывается в точку и взрывается
@Composable
fun BlobIcon() {
    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    IconButton(
        onClick = {
            scope.launch {
                scale.animateTo(0f, tween(180, easing = FastOutLinearInEasing))
                scale.animateTo(1f, spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                ))
            }
        },
        modifier = Modifier
            .size(130.dp)
            .graphicsLayer(scaleX = scale.value, scaleY = scale.value)
    ) {
        SimpleIconForAnim()
    }
}

// 4. RUBBER BAND — резиновый отскок по X/Y
// ─────────────────────────────────────────────
@Composable
fun RubberBandIcon() {
    val scaleX = remember { Animatable(1f) }
    val scaleY = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    IconButton(
        onClick = {
            scope.launch {
                val framesX = listOf(1f, 1.3f, 0.75f, 1.15f, 0.92f, 1.04f, 1f)
                val framesY = listOf(1f, 0.7f, 1.25f, 0.88f, 1.06f, 0.97f, 1f)
                val durations = listOf(80, 90, 90, 90, 90, 100)
                for (i in 1..framesX.lastIndex) {
                    launch { scaleX.animateTo(framesX[i], tween(durations[i-1], easing = LinearEasing)) }
                    scaleY.animateTo(framesY[i], tween(durations[i-1], easing = LinearEasing))
                }
            }
        },
        modifier = Modifier
            .size(130.dp)
            .graphicsLayer(scaleX = scaleX.value, scaleY = scaleY.value)
    ) {
        SimpleIconForAnim()
    }
}

@Composable
fun OrbitBurstIcon() {
    val rotation = remember { Animatable(0f) }
    val particleProgress = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(72.dp)
    ) {


        IconButton(
            onClick = {
                scope.launch {
                    launch {
                        rotation.snapTo(0f)
                        rotation.animateTo(360f, spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        ))
                        rotation.snapTo(0f)
                    }
                    launch {
                        particleProgress.snapTo(0f)
                        particleProgress.animateTo(1f, tween(500, easing = FastOutSlowInEasing))
                    }
                }
            },
            modifier = Modifier
                .size(130.dp)
                .graphicsLayer(rotationZ = rotation.value)
        ) {
            SimpleIconForAnim()
        }
    }
}

@Composable
fun GravityDropIcon() {
    val offsetY = remember { Animatable(0f) }
    val scaleX = remember { Animatable(1f) }
    val scaleY = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    IconButton(
        onClick = {
            scope.launch {
                // падение
                launch { scaleX.animateTo(1.2f, tween(180, easing = FastOutLinearInEasing)) }
                launch { scaleY.animateTo(0.75f, tween(180, easing = FastOutLinearInEasing)) }
                offsetY.animateTo(28f, tween(200, easing = FastOutLinearInEasing))
                // отскок
                launch { scaleX.animateTo(0.88f, tween(120)); scaleX.animateTo(1f, spring(Spring.DampingRatioMediumBouncy)) }
                launch { scaleY.animateTo(1.18f, tween(120)); scaleY.animateTo(1f, spring(Spring.DampingRatioMediumBouncy)) }
                offsetY.animateTo(-8f, tween(130, easing = FastOutSlowInEasing))
                offsetY.animateTo(0f, spring(Spring.DampingRatioMediumBouncy))
            }
        },
        modifier = Modifier
            .size(130.dp)
            .graphicsLayer(
                translationY = offsetY.value,
                scaleX = scaleX.value,
                scaleY = scaleY.value
            )
    ) {
        SimpleIconForAnim()
    }
}

@Composable
fun GlitchIcon() {
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    val skewX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    IconButton(
        onClick = {
            scope.launch {
                val steps = listOf(
                    Triple(-4f, 2f, 4f),
                    Triple(4f, -2f, -3f),
                    Triple(-3f, 3f, 5f),
                    Triple(3f, -1f, -4f),
                    Triple(-2f, 2f, 2f),
                    Triple(2f, 0f, -1f),
                    Triple(-1f, 1f, 0f),
                    Triple(0f, 0f, 0f)
                )
                steps.forEach { (tx, ty, sk) ->
                    launch { offsetX.animateTo(tx, tween(40, easing = LinearEasing)) }
                    launch { offsetY.animateTo(ty, tween(40, easing = LinearEasing)) }
                    skewX.animateTo(sk, tween(40, easing = LinearEasing))
                }
            }
        },
        modifier = Modifier
            .size(130.dp)
            .graphicsLayer(
                translationX = offsetX.value,
                translationY = offsetY.value,
                rotationZ = skewX.value * 0.5f,
                scaleX = 1f + abs(skewX.value) * 0.015f
            )
    ) {
        SimpleIconForAnim()
    }
}

@Composable
fun PendulumSwingIcon() {
    val rotation = remember { Animatable(0f) }
    val trailAlphas = remember { List(3) { Animatable(0f) } }
    val scope = rememberCoroutineScope()

    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(72.dp)) {
        trailAlphas.forEach { anim ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .graphicsLayer(alpha = anim.value)
                    .background(Color(0xFF5DCAA5), CircleShape)
            )
        }
        IconButton(
            onClick = {
                scope.launch {
                    trailAlphas.forEachIndexed { i, anim ->
                        scope.launch {
                            delay(i * 70L)
                            anim.snapTo(0.6f)
                            anim.animateTo(0f, tween(500, easing = FastOutSlowInEasing))
                        }
                    }
                    val frames = listOf(0f, -28f, 20f, -10f, 5f, 0f)
                    val durations = listOf(140, 180, 150, 120, 110)
                    frames.forEachIndexed { i, target ->
                        if (i == 0) return@forEachIndexed
                        rotation.animateTo(target, tween(durations[i-1],
                            easing = FastOutSlowInEasing))
                    }
                }
            },
            modifier = Modifier
                .size(130.dp)
                .graphicsLayer(rotationZ = rotation.value)
        ) {
            SimpleIconForAnim()
        }
    }
}

// 1. SPIRAL IN — закручивается в точку и раскручивается обратно
// ─────────────────────────────────────────────
@Composable
fun SpiralInIcon() {
    val rotation = remember { Animatable(0f) }
    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    IconButton(
        onClick = {
            scope.launch {
                // закрутка в точку
                launch { rotation.animateTo(200f, tween(220, easing = FastOutLinearInEasing)) }
                scale.animateTo(0f, tween(220, easing = FastOutLinearInEasing))
                // телепорт и раскрутка
                rotation.snapTo(-60f)
                launch {
                    rotation.animateTo(0f, spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    ))
                }
                scale.animateTo(1f, spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow
                ))
            }
        },
        modifier = Modifier
            .size(130.dp)
            .graphicsLayer(
                rotationZ = rotation.value,
                scaleX = scale.value,
                scaleY = scale.value
            )
    ) {
        SimpleIconForAnim()
    }
}

// 2. SQUISH WALL — бьётся в правую стену и отлетает
// ─────────────────────────────────────────────
@Composable
fun SquishWallIcon() {
    val offsetX = remember { Animatable(0f) }
    val scaleX = remember { Animatable(1f) }
    val scaleY = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    IconButton(
        onClick = {
            scope.launch {
                // летим вправо и плющимся
                launch { scaleX.animateTo(0.55f, tween(180, easing = FastOutLinearInEasing)) }
                launch { scaleY.animateTo(1.3f, tween(180, easing = FastOutLinearInEasing)) }
                offsetX.animateTo(22f, tween(180, easing = FastOutLinearInEasing))
                // отлетаем влево с пружиной
                launch {
                    scaleX.animateTo(1.1f, tween(100))
                    scaleX.animateTo(1f, spring(Spring.DampingRatioMediumBouncy))
                }
                launch {
                    scaleY.animateTo(0.93f, tween(100))
                    scaleY.animateTo(1f, spring(Spring.DampingRatioMediumBouncy))
                }
                offsetX.animateTo(-8f, tween(150, easing = FastOutSlowInEasing))
                offsetX.animateTo(0f, spring(Spring.DampingRatioMediumBouncy))
            }
        },
        modifier = Modifier
            .size(130.dp)
            .graphicsLayer(
                translationX = offsetX.value,
                scaleX = scaleX.value,
                scaleY = scaleY.value
            )
    ) {
        SimpleIconForAnim()
    }
}

@Composable
fun HeartbeatIcon() {
    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    IconButton(
        onClick = {
            scope.launch {
                // первый удар — сильный
                scale.animateTo(1.3f, tween(100, easing = FastOutLinearInEasing))
                scale.animateTo(1f, tween(120, easing = FastOutSlowInEasing))
                // пауза между ударами
                delay(60)
                // второй удар — слабее
                scale.animateTo(1.18f, tween(90, easing = FastOutLinearInEasing))
                scale.animateTo(1f, spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                ))
            }
        },
        modifier = Modifier
            .size(130.dp)
            .graphicsLayer(scaleX = scale.value, scaleY = scale.value)
    ) {
        SimpleIconForAnim()
    }
}

@Composable
fun WormholeIcon() {
    val scale = remember { Animatable(1f) }
    val rotation = remember { Animatable(0f) }
    val alpha = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    IconButton(
        onClick = {
            scope.launch {
                // засасывает
                launch { rotation.animateTo(360f, tween(350, easing = FastOutLinearInEasing)) }
                launch { alpha.animateTo(0f, tween(320, easing = FastOutLinearInEasing)) }
                scale.animateTo(0f, tween(350, easing = FastOutLinearInEasing))
                // ресет позиции
                rotation.snapTo(0f)
                scale.snapTo(0f)
                // выплёвывает обратно
                launch { alpha.animateTo(1f, tween(280, easing = FastOutSlowInEasing)) }
                scale.animateTo(1f, spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow
                ))
            }
        },
        modifier = Modifier
            .size(130.dp)
            .graphicsLayer(
                scaleX = scale.value,
                scaleY = scale.value,
                rotationZ = rotation.value,
                alpha = alpha.value
            )
    ) {
        SimpleIconForAnim()
    }
}

//5. ELASTIC STRETCH — растягивается по X и резко отпускается
// ─────────────────────────────────────────────
@Composable
fun ElasticStretchIcon() {
    val scaleX = remember { Animatable(1f) }
    val scaleY = remember { Animatable(1f) }
    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    IconButton(
        onClick = {
            scope.launch {
                // натягиваем вправо
                launch { scaleX.animateTo(1.5f, tween(140, easing = FastOutLinearInEasing)) }
                launch { scaleY.animateTo(0.7f, tween(140, easing = FastOutLinearInEasing)) }
                offsetX.animateTo(10f, tween(140, easing = FastOutLinearInEasing))
                // резкий отпуск — пружина
                launch {
                    scaleX.animateTo(0.7f, tween(80))
                    scaleX.animateTo(1f, spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    ))
                }
                launch {
                    scaleY.animateTo(1.3f, tween(80))
                    scaleY.animateTo(1f, spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    ))
                }
                offsetX.animateTo(-6f, tween(90))
                offsetX.animateTo(0f, spring(Spring.DampingRatioMediumBouncy))
            }
        },
        modifier = Modifier
            .size(130.dp)
            .graphicsLayer(
                scaleX = scaleX.value,
                scaleY = scaleY.value,
                translationX = offsetX.value
            )
    ) {
        SimpleIconForAnim()
    }
}