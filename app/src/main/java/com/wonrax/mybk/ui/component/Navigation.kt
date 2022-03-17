package com.wonrax.mybk.ui.component

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.wonrax.mybk.ui.Screen
import com.wonrax.mybk.ui.screens.ExamsScreen
import com.wonrax.mybk.ui.screens.GradesScreen
import com.wonrax.mybk.ui.screens.ProfileScreen
import com.wonrax.mybk.ui.screens.SchedulesScreen
import com.wonrax.mybk.ui.screens.TitleBarScreen
import com.wonrax.mybk.ui.theme.Color
import com.wonrax.mybk.viewmodel.MainActivityViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation(
    navController: NavHostController,
    mainActivityViewModel: MainActivityViewModel
) {
    AnimatedNavHost(navController = navController, startDestination = "home") {
        navigation(startDestination = Screen.Schedules.route, route = "home") {
            composable(
                Screen.Schedules.route,
                enterTransition = {
                    if (initialState.destination.route?.contains("courseDetail") == true)
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(150)
                        )
                    else EnterTransition.None
                },
                exitTransition = {
                    if (targetState.destination.route?.contains("courseDetail") == true)
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(150)
                        )
                    else ExitTransition.None
                }
            ) {
                // Change status bar back to grey when come back from course detail
                val systemUIController = rememberSystemUiController()
                LaunchedEffect(true) {
                    systemUIController.setStatusBarColor(Color.Grey10, darkIcons = true)
                }

                SchedulesScreen(mainActivityViewModel.mybkViewModel) { semester, courseId ->
                    navController.navigate("courseDetail/$semester/$courseId")
                }
            }
            composable(
                Screen.Exams.route,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                ExamsScreen(mainActivityViewModel.mybkViewModel)
            }
            composable(
                Screen.Transcript.route,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                GradesScreen(mainActivityViewModel.mybkViewModel)
            }
            composable(
                Screen.Profile.route,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                ProfileScreen()
            }
        }
        composable(
            "courseDetail/{semester}/{courseId}",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left, animationSpec = tween(150)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right, animationSpec = tween(150)
                )
            },
        ) { backStackEntry ->
            Box(
                Modifier
                    .background(Color.Light)
                    .fillMaxSize()
            ) {
                backStackEntry.arguments?.getString("courseId")
                    ?.let {
                        TitleBarScreen(title = it, navController::popBackStack) {
                            for (i in 1..50) {
                                Text("Hehe", fontSize = FontSize.Large)
                            }
                        }
                    }
            }
        }
    }
}
