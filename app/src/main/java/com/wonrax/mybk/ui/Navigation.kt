package com.wonrax.mybk.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.wonrax.mybk.model.schedule.CourseSchedule
import com.wonrax.mybk.ui.screens.CourseDetailScreen
import com.wonrax.mybk.ui.screens.ExamsScreen
import com.wonrax.mybk.ui.screens.GradesScreen
import com.wonrax.mybk.ui.screens.ProfileScreen
import com.wonrax.mybk.ui.screens.SchedulesScreen
import com.wonrax.mybk.ui.theme.Color
import com.wonrax.mybk.viewmodel.MainActivityViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation(
    navController: NavHostController,
    mainActivityViewModel: MainActivityViewModel
) {
    AnimatedNavHost(navController = navController, startDestination = "home") {

        /* ###################################
        ############ HOME SCREEN #############
        ###################################### */
        navigation(startDestination = Screen.Schedules.route, route = "home") {
            composable(
                Screen.Schedules.route,
                enterTransition = {
                    if (initialState.destination.route?.contains("courseDetail") == true)
                        slideIn()
                    else EnterTransition.None
                },
                exitTransition = {
                    if (targetState.destination.route?.contains("courseDetail") == true)
                        slideOut()
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

        /* ###################################
        ######## COURSE DETAIL SCREEN ########
        ###################################### */
        composable(
            "courseDetail/{semester}/{courseId}",
            enterTransition = { slideIn() },
            exitTransition = { slideOut() },
        ) { backStackEntry ->
            val courseSchedule = remember { mutableStateOf<CourseSchedule?>(null) }
            LaunchedEffect(key1 = true) {
                val courseId = backStackEntry.arguments?.getString("courseId")
                val semester = backStackEntry.arguments?.getString("semester")

                courseSchedule.value = mainActivityViewModel.mybkViewModel.schedulesData.value
                    ?.firstOrNull { it.hk_nh == semester }
                    ?.tkb?.firstOrNull { it.ma_mh == courseId }

                println("$semester $courseId")
                println("${courseSchedule.value?.hk_nh}")
            }
            CourseDetailScreen(courseSchedule = courseSchedule.value, navController::popBackStack)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun AnimatedContentScope<NavBackStackEntry>.slideOut(): ExitTransition {
    return slideOutOfContainer(
        AnimatedContentScope.SlideDirection.Right, animationSpec = tween(150)
    )
}

@OptIn(ExperimentalAnimationApi::class)
fun AnimatedContentScope<NavBackStackEntry>.slideIn(): EnterTransition {
    return slideIntoContainer(
        AnimatedContentScope.SlideDirection.Left, animationSpec = tween(150)
    )
}
