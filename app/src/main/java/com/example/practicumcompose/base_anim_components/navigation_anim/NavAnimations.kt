package com.example.practicumcompose.base_anim_components.navigation_anim

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

interface NavAnimation {
    val enter: EnterTransition
    val exit: ExitTransition
    val popEnter: EnterTransition
    val popExit: ExitTransition
}

object NavAnimations {

    val slideAnimation = object : NavAnimation {
        override val enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn()
        override val exit = slideOutHorizontally( targetOffsetX =  { -it }) + fadeOut()
        override val popEnter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn()
        override val popExit = slideOutHorizontally(targetOffsetX = {it}) + fadeOut()
    }
}