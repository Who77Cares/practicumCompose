package com.example.practicumcompose.base_anim_components.navigation_anim

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseInBack
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically

interface NavAnimation {
    val enter: EnterTransition
    val exit: ExitTransition
    val popEnter: EnterTransition
    val popExit: ExitTransition
}

object NavAnimations {

    // слева направо
    val slideAnimation = object : NavAnimation {
        override val enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn()
        override val exit = slideOutHorizontally( targetOffsetX =  { -it }) + fadeOut()
        override val popEnter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn()
        override val popExit = slideOutHorizontally(targetOffsetX = {it}) + fadeOut()
    }

    // слайд + fade - практически то же самое что и slideAnimation (не заметил разницы)
    val slideFade = object : NavAnimation {
        override val enter = slideInHorizontally(
            initialOffsetX =  { it },
            animationSpec = tween(4000)
        ) + fadeIn(animationSpec = tween(4000)
        )
        override val exit = slideOutHorizontally(
            targetOffsetX = { -it / 3 },
            animationSpec = tween(4000)
        ) + fadeOut(animationSpec = tween(4000))
        override val popEnter = slideInHorizontally(
            initialOffsetX =  { -it / 3  },
            animationSpec = tween(4000)
        ) + fadeIn(animationSpec =  tween(4000))
        override val popExit = slideOutHorizontally(
            targetOffsetX =  { it },
            animationSpec = tween(400)
        ) + fadeOut(animationSpec = tween(400))
    }



    // снизу вверх
    val slideVertical = object : NavAnimation {
        override val enter = slideInVertically(initialOffsetY = { it }) + fadeIn()
        override val exit = fadeOut(animationSpec = tween(100))
        override val popEnter = fadeIn(animationSpec = tween(100))
        override val popExit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
    }

    // чуть более плавная анимация снизу вверх
    val depth = object : NavAnimation {
        override val enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(2000, easing = EaseOutCubic)
        )
        override val exit = slideOutVertically(
            targetOffsetY =  { -it / 4 },
            animationSpec = tween(2000)
        ) + fadeOut(animationSpec = tween(2300))
        override val popEnter = slideInVertically(
            initialOffsetY =  { -it / 4 },
            animationSpec = tween(2000)
        ) + fadeIn(animationSpec = tween(2300))
        override val popExit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(2000, easing = EaseInCubic)
        )
    }






    // выезд-заезд из центра
    val scale = object : NavAnimation {
        override val enter = scaleIn(initialScale = 0.8f) + fadeIn()
        override val exit = scaleOut(targetScale = 0.8f) + fadeOut()
        override val popEnter = scaleIn(initialScale = 0.8f) + fadeIn()
        override val popExit = scaleOut(targetScale = 0.8f) + fadeOut()
    }

    // scale (4 анимация) + эффект легкой пружинки
    val expand = object : NavAnimation {
        override val enter = scaleIn(
            initialScale = 0.6f,
            animationSpec = tween(400, easing = EaseOutBack)
        ) + fadeIn(animationSpec = tween(300))
        override val exit = scaleOut(
            targetScale = 1.05f,
            animationSpec = tween(300)
        ) + fadeOut(animationSpec = tween(300))
        override val popEnter = scaleIn(
            initialScale = 1.05f,
            animationSpec = tween(300)
        ) + fadeIn(animationSpec = tween(300))
        override val popExit = scaleOut(
            targetScale = 0.6f,
            animationSpec = tween(400, easing = EaseInBack)
        ) + fadeOut(animationSpec = tween(300))
    }

    // плавное затухание-заезд в центр
    val flip = object : NavAnimation {
        override val enter = scaleIn(
            initialScale = 0.0f,
            animationSpec = tween(400, easing = EaseOutCubic)
        ) + fadeIn(animationSpec = tween(300, delayMillis = 400))
        override val exit = scaleOut(
            targetScale = 0.0f,
            animationSpec = tween(400, easing = EaseInCubic)
        ) + fadeOut(animationSpec = tween(300))
        override val popEnter = scaleIn(
            initialScale = 0.0f,
            animationSpec = tween(300, easing = EaseOutCubic)
        ) + fadeIn(animationSpec = tween(300, delayMillis = 400))
        override val popExit = scaleOut(
            targetScale = 0.0f,
            animationSpec = tween(400, easing = EaseInCubic)
        ) + fadeOut(animationSpec = tween(300))
    }


    // похожа на expand но без эффекта пружинки и чуть более плавная
    val scaleFade = object : NavAnimation {
        override val enter = scaleIn(
            initialScale = 1.2f,
            animationSpec = tween(400, easing = EaseOutCubic)
        ) + fadeIn(animationSpec = tween(300))
        override val exit = scaleOut(
            targetScale = 0.9f,
            animationSpec = tween(400, easing = EaseInCubic)
        ) + fadeOut(animationSpec = tween(300))
        override val popEnter = scaleIn(
            initialScale = 0.9f,
            animationSpec = tween(300, easing = EaseOutCubic)
        ) + fadeIn(animationSpec = tween(300))
        override val popExit = scaleOut(
            targetScale = 1.2f,
            animationSpec = tween(400, easing = EaseInCubic)
        ) + fadeOut(animationSpec = tween(300))
    }


}