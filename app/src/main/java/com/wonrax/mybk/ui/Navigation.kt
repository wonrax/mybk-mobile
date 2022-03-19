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
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.dialog
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.wonrax.mybk.model.schedule.CourseSchedule
import com.wonrax.mybk.ui.screens.CourseDetailScreen
import com.wonrax.mybk.ui.screens.ExamsScreen
import com.wonrax.mybk.ui.screens.FeedbackDialog
import com.wonrax.mybk.ui.screens.GradesScreen
import com.wonrax.mybk.ui.screens.PolicyScreen
import com.wonrax.mybk.ui.screens.ProfileScreen
import com.wonrax.mybk.ui.screens.SchedulesScreen
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
                enterTransition = { homeRouteEnterAnim() },
                exitTransition = { homeRouteExitAnim() }
            ) { from ->
                SchedulesScreen(mainActivityViewModel.mybkViewModel) { semester, courseId ->
                    // In order to discard duplicated navigation events, we check the Lifecycle
                    if (from.lifecycleIsResumed()) {
                        navController.navigate("courseDetail/$semester/$courseId")
                    }
                }
            }
            composable(
                Screen.Exams.route,
                enterTransition = { homeRouteEnterAnim() },
                exitTransition = { homeRouteExitAnim() }
            ) {
                ExamsScreen(mainActivityViewModel.mybkViewModel)
            }
            composable(
                Screen.Transcript.route,
                enterTransition = { homeRouteEnterAnim() },
                exitTransition = { homeRouteExitAnim() }
            ) {
                GradesScreen(mainActivityViewModel.mybkViewModel)
            }
            composable(
                Screen.Profile.route,
                enterTransition = { homeRouteEnterAnim() },
                exitTransition = { homeRouteExitAnim() }
            ) {
                ProfileScreen(
                    mainActivityViewModel,
                    { navController.navigate("policy") { launchSingleTop = true } },
                    { navController.navigate("feedback") { launchSingleTop = true } }
                )
            }
        }

        /* ###################################
        ######## COURSE DETAIL SCREEN ########
        ###################################### */
        composable(
            "courseDetail/{semester}/{courseId}",
            enterTransition = { slideInLeft() },
            exitTransition = { slideOutRight() },
        ) { backStackEntry ->
            // TODO bring this to CourseDetailScreen
            val courseSchedule = remember { mutableStateOf<CourseSchedule?>(null) }
            LaunchedEffect(key1 = true) {
                val courseId = backStackEntry.arguments?.getString("courseId")
                val semester = backStackEntry.arguments?.getString("semester")

                courseSchedule.value = mainActivityViewModel.mybkViewModel.schedulesData.value
                    ?.firstOrNull { it.hk_nh == semester }
                    ?.tkb?.firstOrNull { it.ma_mh == courseId }
            }
            CourseDetailScreen(courseSchedule = courseSchedule.value, navController::popBackStack)
        }

        /* ###################################
        ########### POLICY SCREEN ############
        ###################################### */
        composable(
            "policy",
            enterTransition = { slideInLeft() },
            exitTransition = { slideOutRight() },
        ) {
            PolicyScreen(navController::popBackStack)
        }

        /* ###################################
        ########## FEEDBACK DIALOG ###########
        ###################################### */
        dialog("feedback") {
            FeedbackDialog(upAction = navController::popBackStack)
        }
    }
}

fun isHomeSubRoutes(route: String?): Boolean {
    if (route in listOf(
            Screen.Schedules.route,
            Screen.Exams.route,
            Screen.Transcript.route,
            Screen.Profile.route
        )
    ) return true
    return false
}

@OptIn(ExperimentalAnimationApi::class)
fun AnimatedContentScope<NavBackStackEntry>.slideOutRight(): ExitTransition {
    return slideOutOfContainer(
        AnimatedContentScope.SlideDirection.Right, animationSpec = tween(150)
    )
}

@OptIn(ExperimentalAnimationApi::class)
fun AnimatedContentScope<NavBackStackEntry>.slideOutLeft(): ExitTransition {
    return slideOutOfContainer(
        AnimatedContentScope.SlideDirection.Left, animationSpec = tween(150)
    )
}

@OptIn(ExperimentalAnimationApi::class)
fun AnimatedContentScope<NavBackStackEntry>.slideInRight(): EnterTransition {
    return slideIntoContainer(
        AnimatedContentScope.SlideDirection.Right, animationSpec = tween(150)
    )
}

@OptIn(ExperimentalAnimationApi::class)
fun AnimatedContentScope<NavBackStackEntry>.slideInLeft(): EnterTransition {
    return slideIntoContainer(
        AnimatedContentScope.SlideDirection.Left, animationSpec = tween(150)
    )
}

@OptIn(ExperimentalAnimationApi::class)
fun AnimatedContentScope<NavBackStackEntry>.homeRouteEnterAnim(): EnterTransition {
    return if (isHomeSubRoutes(initialState.destination.route))
        EnterTransition.None
    else slideInRight()
}

@OptIn(ExperimentalAnimationApi::class)
fun AnimatedContentScope<NavBackStackEntry>.homeRouteExitAnim(): ExitTransition {
    return if (isHomeSubRoutes(targetState.destination.route))
        ExitTransition.None
    else slideOutLeft()
}

/**
 * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
 *
 * This is used to de-duplicate navigation events.
 */
private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED
