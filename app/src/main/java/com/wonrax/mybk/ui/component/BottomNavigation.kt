package com.wonrax.mybk.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.wonrax.mybk.ui.Screen
import com.wonrax.mybk.ui.theme.Color

@Composable
fun BottomNavigation(navController: NavHostController, onItemClick: (String) -> Unit) {
    val items = Screen.Items.list
    val backStackEntry = navController.currentBackStackEntryAsState()

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
            val selected = screen.id == backStackEntry.value?.destination?.route
            val iconColor = if (selected) Color.Primary else Color.Grey50

            Column(
                modifier = Modifier
                    .padding(1.dp, 2.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onItemClick(screen.id) }
                    .fillMaxHeight()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    icon = if (selected) screen.iconSelected else screen.icon,
                    tint = iconColor
                )
                AnimatedVisibility(visible = selected) {
                    Text(
                        screen.title,
                        fontSize = FontSize.Small,
                        color = iconColor,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        softWrap = false
                    )
                }
            }
        }
    }
}
