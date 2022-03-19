package com.wonrax.mybk.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.wonrax.mybk.ui.Screen
import com.wonrax.mybk.ui.theme.Color

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
        Row(
            modifier = Modifier
                .padding(0.dp)
                .background(Color.Light)
                .fillMaxWidth()
                .height(56.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEach { screen ->
                val selected = screen.route == backStackEntry.value?.destination?.route

                Column(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = false),
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
                    Text(
                        screen.title,
                        fontSize = FontSize.Small,
                        color = Color.Dark,
                        textAlign = TextAlign.Center,
                        softWrap = false,
                        letterSpacing = (-0.2).sp
                    )
                }
            }
        }
    }
}
