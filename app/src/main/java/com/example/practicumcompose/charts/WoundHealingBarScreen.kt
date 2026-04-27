package com.example.practicumcompose.charts

import android.graphics.Color.parseColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun WoundHealingBarScreen() {
    val dataset    = WoundHealingDataset
    val days       = dataset.days
    val treatments = dataset.treatments

    // один Animatable на каждый столбец (treatments.size * days.size)
    // анимируем progress 0f → 1f, высоту считаем в Canvas
    val animatables = remember {
        List(treatments.size * days.size) { Animatable(0f) }
    }

    LaunchedEffect(Unit) {
        // запускаем группами по суткам — сутки появляются последовательно
        days.forEachIndexed { dayIndex, _ ->
            treatments.forEachIndexed { tIndex, _ ->
                val idx = tIndex * days.size + dayIndex
                launch {
                    delay((dayIndex * 80).toLong())
                    animatables[idx].animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
                    )
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Площадь раны по суткам",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "мм² · каждая группа столбцов = одни сутки",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
        )

        WoundBarChart(
            days       = days,
            treatments = treatments,
            animatables = animatables,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        Spacer(Modifier.height(20.dp))

        // легенда
        treatments.forEach { t ->
            val color = remember(t.color) { Color(parseColor(t.color)) }
            androidx.compose.foundation.layout.Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                androidx.compose.foundation.Canvas(
                    Modifier
                        .height(12.dp)
                        .padding(end = 8.dp)
                        .then(Modifier.height(12.dp))
                ) { }
                androidx.compose.foundation.layout.Box(
                    Modifier
                        .height(3.dp)
                        .then(Modifier.padding(end = 8.dp))
                )
                LegendDot(color = color)
                androidx.compose.foundation.layout.Spacer(Modifier.padding(4.dp))
                Text(
                    text = t.name,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "${t.finalValue().toInt()} мм² (день 14)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun LegendDot(color: Color) {
    Canvas(modifier = Modifier.height(12.dp).then(Modifier.padding(end = 8.dp))) {
        drawRoundRect(
            color = color,
            size = Size(20.dp.toPx(), 10.dp.toPx()),
            cornerRadius = CornerRadius(2.dp.toPx())
        )
    }
}

@Composable
fun WoundBarChart(
    days: List<Int>,
    treatments: List<Treatment>,
    animatables: List<Animatable<Float, *>>,
    modifier: Modifier = Modifier
) {
    val maxValue    = 460f
    val labelColor  = android.graphics.Color.parseColor("#888888")

    val treatmentColors = remember(treatments) {
        treatments.map { Color(parseColor(it.color)) }
    }

    // читаем значения прогресса — Compose перерисует при каждом изменении
    val progresses = animatables.map { it.value }

    Canvas(modifier = modifier) {
        val padLeft    = 44.dp.toPx()
        val padBottom  = 28.dp.toPx()
        val padTop     = 12.dp.toPx()
        val padRight   = 8.dp.toPx()

        val chartW = size.width - padLeft - padRight
        val chartH = size.height - padBottom - padTop

        val groupCount  = days.size
        val barCount    = treatments.size
        val groupWidth  = chartW / groupCount
        val barPadding  = 3.dp.toPx()
        val barWidth    = (groupWidth - barPadding * (barCount + 1)) / barCount

        // ── сетка ──
        val ySteps = listOf(0, 100, 200, 300, 400)
        ySteps.forEach { step ->
            val y = padTop + chartH * (1f - step / maxValue)
            drawLine(
                color = Color(0xFFE0E0E0),
                start = Offset(padLeft, y),
                end   = Offset(padLeft + chartW, y),
                strokeWidth = 1f
            )
            drawContext.canvas.nativeCanvas.drawText(
                step.toString(),
                padLeft - 6.dp.toPx(),
                y + 4.dp.toPx(),
                android.graphics.Paint().apply {
                    color = labelColor
                    textSize = 10.sp.toPx()
                    textAlign = android.graphics.Paint.Align.RIGHT
                    isAntiAlias = true
                }
            )
        }

        // ── столбцы ──
        days.forEachIndexed { dayIndex, day ->
            val groupLeft = padLeft + dayIndex * groupWidth

            treatments.forEachIndexed { tIndex, treatment ->
                val value   = treatment.valueAt(day)?.toFloat() ?: 0f
                val animIdx = tIndex * days.size + dayIndex
                val progress = progresses[animIdx]

                val fullBarH = chartH * (value / maxValue)
                val animBarH = fullBarH * progress

                val left = groupLeft + barPadding + tIndex * (barWidth + barPadding)
                val top  = padTop + chartH - animBarH

                drawRoundRect(
                    color        = treatmentColors[tIndex],
                    topLeft      = Offset(left, top),
                    size         = Size(barWidth, animBarH),
                    cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx())
                )
            }

            // X-подпись (сутки)
            drawContext.canvas.nativeCanvas.drawText(
                day.toString(),
                groupLeft + groupWidth / 2,
                size.height - 6.dp.toPx(),
                android.graphics.Paint().apply {
                    color = labelColor
                    textSize = 10.sp.toPx()
                    textAlign = android.graphics.Paint.Align.CENTER
                    isAntiAlias = true
                }
            )
        }
    }
}