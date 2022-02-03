package com.wonrax.mybk.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.wonrax.mybk.ui.component.BottomNavigation
import com.wonrax.mybk.ui.component.Navigation
import com.wonrax.mybk.ui.theme.Color
import com.wonrax.mybk.ui.theme.MybkTheme
import com.wonrax.mybk.viewmodel.SchedulesViewModel

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
                    .background(Color.Grey10)
                    .fillMaxSize(),
                color = Color.Grey10, // TODO how to merge these two backgrounds
            ) {
                Navigation(navController, schedulesViewModel)
            }
        }
    }
}
