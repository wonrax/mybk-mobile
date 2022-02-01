package com.wonrax.mybk.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wonrax.mybk.Greeting
import com.wonrax.mybk.ui.component.BottomNavigation
import com.wonrax.mybk.ui.component.ScheduleCard
import com.wonrax.mybk.ui.component.Screen
import com.wonrax.mybk.ui.theme.MybkColors
import com.wonrax.mybk.ui.theme.MybkTheme
import com.wonrax.mybk.viewmodels.SchedulesViewModel

@Composable
fun MybkUI(schedulesViewModel: SchedulesViewModel) {
    MybkTheme(false) {
        val navController = rememberNavController()
        Scaffold(
            bottomBar = {
                BottomNavigation(navController) {
                    navController.navigate(it) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }

                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true

                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            }
        ) {
            Surface(
                modifier = Modifier
                    .background(MybkColors.Grey10)
                    .padding(8.dp)
                    .fillMaxSize(),
                color = MybkColors.Grey10 // TODO how to merge these two backgrounds
            ) {
                Navigation(navController, schedulesViewModel)
            }
        }
    }
}

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
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (schedulesViewModel.isLoading.value) {
                        CircularProgressIndicator()
                        Text(text = "Loading...")
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            schedulesViewModel.response.value?.forEach { semester ->
                                semester.tkb?.forEach { schedule ->
                                    item {
                                        ScheduleCard(schedule)
                                    }
                                }
                            }
                        }
                    }
                }
            }
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
