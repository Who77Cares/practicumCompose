package com.example.practicumcompose.base_anim_components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun SelectableChipItem(
    modifier: Modifier = Modifier, // применять ТОЛЬКО на корневой элемент!!
    selected: Boolean,
    title: String = "Твой заголовок",
    titleColor: Color =
        if(selected) Color.Blue else Color.Black,
    titleSize: TextUnit = 14.sp,
    titleWeight: FontWeight = FontWeight.Medium,
    subtitle: String? = null,
    borderWidth: Dp = 1.dp,
    borderColor: Color = if(selected) Color.Black else Color.White,
    borderShape: Shape = RoundedCornerShape(size = 12.dp),
    icon: ImageVector = Icons.Default.Add,
    iconColor: Color = if(selected) Color.Blue else Color.Black,
    onClick: () -> Unit
) {

    val scaleA = remember { Animatable(initialValue = 1f) }
    val scaleB = remember { Animatable(initialValue = 1f) }

    LaunchedEffect(key1 = selected) {


        launch {
            scaleA.animateTo(
                targetValue = 0.3f,
                animationSpec = tween(
                    durationMillis = 50
                )
            )
            scaleA.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }

        launch {
            scaleB.animateTo(
                targetValue = 0.9f,
                animationSpec = tween(
                    durationMillis = 50
                )
            )
            scaleB.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }

    }



    Column(
        modifier = modifier
            .wrapContentSize()
            .scale(scaleB.value)
            .border(width = borderWidth, color = borderColor, shape = borderShape)
            .clip(borderShape) // обрезаем рипл-ффект
            .clickable{ onClick() }
    ) {
        Row(
            modifier = Modifier.padding(start =  12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(8f),
                text =  title,
                style = TextStyle(
                    color = titleColor,
                    fontSize = titleSize,
                    fontWeight = titleWeight
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            IconButton(
                modifier = Modifier.weight(2f).scale(scale = scaleA.value),
                onClick = onClick
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor
                )
            }
        }

        if (subtitle != null) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
                text = subtitle,
                style = TextStyle(
                    color = titleColor,
                    fontSize = titleSize,
                    fontWeight = titleWeight
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }

    }
}