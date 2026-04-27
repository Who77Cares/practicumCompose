package com.example.practicumcompose.charts

import android.graphics.Color.parseColor
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt

@Composable
fun WoundHealingChartScreen() {
    val dataset = WoundHealingDataset
    val days = dataset.days                         // [1, 3, 5, 7, 9, 14]
    val treatments = dataset.treatments

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Динамика заживления ран",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Площадь раны, мм² · сутки наблюдения",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
        )

        WoundChart(
            days = days,
            treatments = treatments,
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        )

        Spacer(Modifier.height(20.dp))

        // легенда
        treatments.forEach { t ->
            val color = remember(t.color) { Color(t.color.toColorInt()) }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Box(
                    Modifier
                        .size(width = 20.dp, height = 3.dp)
                        .background(color, RoundedCornerShape(2.dp))
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = t.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "${t.finalValue().toInt()} мм²",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun WoundChart(
    days: List<Int>,
    treatments: List<Treatment>,
    modifier: Modifier = Modifier
) {
    val gridColor   = Color(0xFFE0E0E0)
    val labelColor  = android.graphics.Color.parseColor("#888888")
    val maxY        = 460f
    val minY        = 0f

    val treatmentColors = remember(treatments) {
        treatments.map { Color(parseColor(it.color)) }
    }

    Canvas(modifier = modifier) {
        val padLeft   = 48.dp.toPx()
        val padBottom = 28.dp.toPx()
        val padTop    = 12.dp.toPx()
        val padRight  = 12.dp.toPx()

        val chartW = size.width - padLeft - padRight
        val chartH = size.height - padBottom - padTop

        // helpers
        fun xOf(index: Int) = padLeft + index * (chartW / (days.size - 1).toFloat())
        fun yOf(value: Double) = padTop + chartH * (1f - ((value - minY) / (maxY - minY)).toFloat())

        // ── горизонтальные линии сетки ──
        val ySteps = listOf(0, 100, 200, 300, 400)
        ySteps.forEach { step ->
            val y = yOf(step.toDouble())
            drawLine(gridColor, Offset(padLeft, y), Offset(padLeft + chartW, y), strokeWidth = 1f)
            // Y-подписи
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

        // ── X-подписи (сутки) ──
        days.forEachIndexed { i, day ->
            val x = xOf(i)
            drawContext.canvas.nativeCanvas.drawText(
                day.toString(),
                x,
                size.height - 4.dp.toPx(),
                android.graphics.Paint().apply {
                    color = labelColor
                    textSize = 10.sp.toPx()
                    textAlign = android.graphics.Paint.Align.CENTER
                    isAntiAlias = true
                }
            )
        }

        // ── линии препаратов ──
        treatments.forEachIndexed { tIndex, treatment ->
            val lineColor = treatmentColors[tIndex]

            val path = Path()
            days.forEachIndexed { i, day ->
                val value = treatment.valueAt(day) ?: return@forEachIndexed
                val x = xOf(i)
                val y = yOf(value)
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }

            drawPath(path, color = lineColor, style = Stroke(width = 2.5.dp.toPx()))

            // точки
            days.forEachIndexed { i, day ->
                val value = treatment.valueAt(day) ?: return@forEachIndexed
                drawCircle(
                    color = lineColor,
                    radius = 4.dp.toPx(),
                    center = Offset(xOf(i), yOf(value))
                )
                drawCircle(
                    color = Color.White,
                    radius = 2.dp.toPx(),
                    center = Offset(xOf(i), yOf(value))
                )
            }
        }
    }
}