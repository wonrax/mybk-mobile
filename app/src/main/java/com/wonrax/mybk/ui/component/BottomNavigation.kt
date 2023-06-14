package com.wonrax.mybk.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.wonrax.mybk.ui.Screen
import com.wonrax.mybk.ui.theme.Color
import kotlinx.coroutines.delay
import java.util.Calendar
import java.util.Date

@Composable
fun BottomNavigation(navController: NavHostController) {
    val items = Screen.Items.list
    val backStackEntry = navController.currentBackStackEntryAsState()

    val onItemClick = { route: String ->
        navController.navigate(route) {
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

    Surface(elevation = 24.dp) {
        Column {
            StatusLine()
            Divider(Color.Grey10)
            Row(
                modifier = Modifier
                    .padding(0.dp)
                    .background(Color.Light)
                    .fillMaxWidth()
                    .height(48.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { screen ->
                    val selected = screen.route == backStackEntry.value?.destination?.route

                    Column(
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onItemClick(screen.route) }
                            .fillMaxHeight()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            icon = if (selected) screen.iconSelected else screen.icon,
                            tint = Color.Dark
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatusLine() {
    val weekOfYear = remember { mutableStateOf(0) }
    val dayOfWeek = remember { mutableStateOf(1) }

    LaunchedEffect(true) {
        fun updateTime() {
            val calendar = Calendar.getInstance()

            calendar.minimalDaysInFirstWeek = 4
            calendar.firstDayOfWeek = Calendar.MONDAY

            /* Set date */
            calendar.time = Date()

            weekOfYear.value = calendar.get(Calendar.WEEK_OF_YEAR)
            dayOfWeek.value = calendar.get(Calendar.DAY_OF_WEEK)
        }

        // Update the time indicator every minute
        while (true) {
            updateTime()
            delay(1000 * 60)
        }
    }

    Box(
        Modifier
            .fillMaxWidth()
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Tuần ${weekOfYear.value}",
                fontWeight = FontWeight.Bold,
                fontSize = FontSize.Small
            )
            if (dayOfWeek.value != 1) {
                Text(
                    "Thứ ${dayOfWeek.value}",
                    fontSize = FontSize.Small
                )
            } else {
                Text(
                    "Chủ nhật",
                    fontSize = FontSize.Small
                )
            }
        }
    }
}
