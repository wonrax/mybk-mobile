package com.wonrax.mybk.ui.component

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.wonrax.mybk.Greeting
import com.wonrax.mybk.ui.Screen
import com.wonrax.mybk.ui.screens.SchedulesScreen
import com.wonrax.mybk.viewmodel.SchedulesViewModel

@Composable
fun Navigation(
    navController: NavHostController,
    schedulesViewModel: SchedulesViewModel
) {
    NavHost(navController = navController, startDestination = Screen.Schedules.id) {

//        val home = Screen.Home
        val schedules = Screen.Schedules
        val exams = Screen.Exams
        val transcript = Screen.Transcript
        val profile = Screen.Profile

        composable(schedules.id) {
            SchedulesScreen(schedulesViewModel)
        }
        composable(exams.id) {
            Greeting(name = exams.title)
        }
        composable(transcript.id) {
            Greeting(name = transcript.title)
        }
        composable(profile.id) {
            Greeting(name = profile.title)
        }
    }
}
