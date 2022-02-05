package com.wonrax.mybk.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarResult
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.wonrax.mybk.ui.component.BottomNavigation
import com.wonrax.mybk.ui.component.Navigation
import com.wonrax.mybk.ui.theme.Color
import com.wonrax.mybk.ui.theme.MybkTheme
import com.wonrax.mybk.viewmodel.MainActivityViewModel

@Composable
fun MybkUI(mainActivityViewModel: MainActivityViewModel) {
    MybkTheme(false) {
        val navController = rememberNavController()

        // SnackBar dependencies
        val scaffoldState = rememberScaffoldState()
        val launchSnackBar: suspend (String) -> SnackbarResult = { message ->
            scaffoldState.snackbarHostState.showSnackbar(
                message,
                "OK",
                SnackbarDuration.Indefinite
            )
        }

        Scaffold(
            scaffoldState = scaffoldState,
            snackbarHost = {
                // reuse default SnackbarHost to have default animation and timing handling
                SnackbarHost(it) { data ->
                    // custom snackbar with the custom border
                    Snackbar(
                        shape = RoundedCornerShape(24.dp),
                        actionColor = Color.Primary,
                        snackbarData = data,
                        elevation = 12.dp
                    )
                }
            },
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
            Box(
                modifier = Modifier
                    .background(Color.Grey10)
                    .fillMaxSize(),
            ) {
                Navigation(navController, mainActivityViewModel)
            }

            // Showing or not showing SnackBar
            if (mainActivityViewModel.snackBarState.value.isShowingSnackbar) {
                LaunchedEffect(mainActivityViewModel.snackBarState.value.isShowingSnackbar) {
                    val snackBarResult = mainActivityViewModel.snackBarState.value.message?.let { it1 ->
                        launchSnackBar(
                            it1
                        )
                    }

                    if (snackBarResult == SnackbarResult.ActionPerformed) {
                        mainActivityViewModel.snackBarState.value.onAction?.invoke()
                        mainActivityViewModel.dismissSnackBar()
                    }
                }
            }
        }
    }
}
