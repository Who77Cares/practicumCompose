package com.example.practicumcompose.base_anim_components.navigation_anim

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay


object Routes {
    const val SPLASH = "splash"
    const val MAIN_SCREEN = "main screen"
    const val SECOND_SCREEN = "second screen"
}

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
        enterTransition = {
            NavAnimations.slideAnimation.enter
        },
        exitTransition = {
            NavAnimations.slideAnimation.exit
        },
        popEnterTransition = {
            NavAnimations.slideAnimation.popEnter
        },
        popExitTransition = {
            NavAnimations.slideAnimation.popExit
        }
    ) {
        composable(Routes.SPLASH) {
            LaunchedEffect(Unit) {
                delay(4000L)
                navController.navigate(Routes.MAIN_SCREEN) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            }
            SplashScreen()
        }

        composable(
            route = Routes.MAIN_SCREEN,
        ) {

            MainScreen(
                onClick = {
                    navController.navigate(Routes.SECOND_SCREEN)
                }
            )
        }

        composable(Routes.SECOND_SCREEN) {
            SecondScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

    }

}