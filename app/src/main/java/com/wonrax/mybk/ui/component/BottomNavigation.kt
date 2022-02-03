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
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.wonrax.mybk.R
import com.wonrax.mybk.ui.theme.Color

// TODO refactor this file

@Composable
fun MyAppIcon(
    resourceId: Int,
    modifier: Modifier = Modifier,
    tint: androidx.compose.ui.graphics.Color = MaterialTheme.colors.primary
) {
    Icon(
        painter = painterResource(id = resourceId),
        contentDescription = "",
        modifier = modifier,
        tint = tint
    )
}

// TODO include composable for each screen
sealed class Screen(
    val id: String,
    val title: String,
    val icon: Int,
) {
//    object Home : Screen("home", "Trang chủ", R.drawable.ic_home)
    object Schedules : Screen("schedules", "Giờ học", R.drawable.ic_timecircle)
    object Exams : Screen("exams", "Lịch thi", R.drawable.ic_calendar)
    object Transcript : Screen("transcript", "Bảng điểm", R.drawable.ic_transcipt)
    object Profile : Screen("profile", "Cá nhân", R.drawable.ic_profile)

    object Items {
        val list = listOf(
//            Home,
            Schedules,
            Exams,
            Transcript,
            Profile
        )
    }
}

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
                MyAppIcon(
                    resourceId = screen.icon,
                    tint = iconColor
                )
                AnimatedVisibility(visible = selected) {
                    Text(
                        screen.title,
                        fontSize = FontSize.Small,
                        color = iconColor,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
@Preview(widthDp = 393)
fun P1() {
//    BottomNavigation {}
}
