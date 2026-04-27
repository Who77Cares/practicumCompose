package com.example.practicumcompose.charts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp





@Composable
fun WoundHealingScreen() {
    val rows: List<TreatmentSummary> = remember { WoundHealingDataset.summaryRows() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(rows) { _, row ->
            TreatmentRow(summary = row)
        }
    }
}

@Composable
fun TreatmentRow(summary: TreatmentSummary) {
    val dotColor = remember(summary.color) {
        Color(android.graphics.Color.parseColor(summary.color))
    }
    val reductionText = summary.reductionPercent
        ?.let { "−${it.toInt()}%" }
        ?: "—"
    val isGood = (summary.reductionPercent ?: 0.0) >= 50.0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // цветной маркер препарата
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(dotColor, RoundedCornerShape(2.dp))
            )

            Spacer(Modifier.width(12.dp))

            // название + скорость заживления
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = summary.name,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "↓ ${"%.1f".format(summary.avgHealingRate)} мм²/сут",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // финальное значение + процент снижения
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${summary.finalValue.toInt()} мм²",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = reductionText,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isGood) Color(0xFF1D9E75) else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}