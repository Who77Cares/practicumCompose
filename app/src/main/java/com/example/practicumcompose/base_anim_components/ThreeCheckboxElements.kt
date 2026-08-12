package com.example.practicumcompose.base_anim_components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ThreeCheckboxElements() {

    var isChecked by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {

        AnimatedCheckboxSpring2(
            checked = isChecked,
            onCheckedChange = { isChecked = it }
        )

        HorizontalDivider(thickness = 4.dp)

        RadioButtonCheck()

        HorizontalDivider(thickness = 4.dp)

        Slider()
        HorizontalDivider(thickness = 4.dp)

        TabContainer()

    }

}

@Composable
fun AnimatedCheckboxSpring2(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale"
    )

    val borderColor by animateColorAsState(
        targetValue = if (checked) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.outline,
        animationSpec = tween(200),
        label = "border"
    )

    val checkColor = MaterialTheme.colorScheme.primary

    Canvas(
        modifier = modifier
            .size(24.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onCheckedChange(!checked) }
    ) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        val corner = CornerRadius(4.dp.toPx())

        // Рамка
        drawRoundRect(color = borderColor, style = stroke, cornerRadius = corner)

        // Заливка масштабируется от центра (spring bounce)
        if (scale > 0f) {
            scale(scale) {
                drawRoundRect(color = checkColor, cornerRadius = corner)
            }
        }

        // Галочка появляется после заливки
        val checkProgress = ((scale - 0.5f) / 0.5f).coerceIn(0f, 1f)
        if (checkProgress > 0f) {
            val path = Path().apply {
                moveTo(size.width * 0.2f, size.height * 0.5f)
                lineTo(size.width * 0.42f, size.height * 0.72f)
                lineTo(size.width * 0.78f, size.height * 0.28f)
            }
            val measure = PathMeasure()
            measure.setPath(path, false)
            val segment = Path()
            measure.getSegment(0f, measure.length * checkProgress, segment, true)

            drawPath(
                path = segment,
                color = Color.White,
                style = Stroke(width = 2.2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
        }
    }
}

@Composable
fun RadioButtonCheck() {
    val options = listOf("Низкий", "Средний", "Высокий")
    var selectedOption by remember { mutableStateOf(options[0]) }

    options.forEach { option ->
        Row(
            modifier = Modifier.clickable { selectedOption = option }
        ) {
            RadioButton(
                selected = (selectedOption == option),
                onClick = { selectedOption = option }
            )
            Text(option)
        }
    }

}

@Composable
fun Slider() {
    var value by remember { mutableStateOf(0f) }
    val steps = 3 // 3 позиции

    Slider(
        value = value,
        onValueChange = { value = it },
        valueRange = 0f..steps.toFloat(),
        steps = steps - 1,
        colors = SliderDefaults.colors(
            thumbColor = MaterialTheme.colorScheme.primary
        )
    )
// Добавляем метки
    Row {
        listOf("1", "2", "3").forEach { Text(it, modifier = Modifier.weight(1f)) }
    }
}


// четвертый комонент

data class TabItem(
    val label: String,
    val icon: ImageVector
)

@Composable
private fun PillTab(
    tab: TabItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    val containerColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = tween(300),
        label = "containerColor"
    )

    val contentColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(300),
        label = "contentColor"
    )

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = containerColor,
        contentColor = contentColor,
        modifier = Modifier.height(56.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .animateContentSize(animationSpec = tween(300)), // ← только тут, единственный аниматор размера
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(tab.icon, contentDescription = null)

            AnimatedVisibility(
                visible = selected,
                enter = fadeIn(tween(200)) + expandHorizontally(tween(300)),
                exit = fadeOut(tween(150)) + shrinkHorizontally(tween(300))
            ) {
                Text(tab.label)
            }
        }
    }
}

@Composable
fun ExpandingTabBar(
    tabs: List<TabItem>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .wrapContentSize()
            .padding(8.dp)
            .background(Color.Red),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEachIndexed { index, tab ->
            PillTab(
                tab = tab,
                selected = index == selectedIndex,
                onClick = { onTabSelected(index) }
            )
        }
    }
}

@Composable
fun TabContainer() {

    var selectedIndex by remember { mutableIntStateOf(0) }

    val tabs = remember {
        listOf(
            TabItem("Главная", Icons.Default.Person),
            TabItem("Поиск", Icons.Default.Face),
            TabItem("Профиль", Icons.Default.AddCircle)
        )
    }

    ExpandingTabBar(
        tabs = tabs,
        selectedIndex = selectedIndex,
        onTabSelected = { selectedIndex = it }
    )
}
