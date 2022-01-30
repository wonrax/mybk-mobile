package com.wonrax.mybk.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wonrax.mybk.R

@Composable
fun MyAppIcon(
    resourceId: Int,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colors.primary
) {
    Icon(
        painter = painterResource(id = resourceId),
        contentDescription = "",
        modifier = modifier,
        tint = tint
    )
}

sealed class Screen(
    val id: String,
    val title: String,
    val icon: Int,
) {
    object Home : Screen("home", "Trang chủ", R.drawable.ic_home)
    object Schedules : Screen("schedules", "Lịch học", R.drawable.ic_calendar)
    object Transcript : Screen("transcript", "Bảng điểm", R.drawable.ic_document)
    object Profile : Screen("profile", "Cá nhân", R.drawable.ic_profile)

    object Items {
        val list = listOf(
            Home, Schedules, Transcript, Profile
        )
    }
}

@Composable
fun BottomNavigation(onItemClick: (String) -> Unit) {
    val items = Screen.Items.list

    Row(
        modifier = Modifier
            .padding(0.dp)
            .background(MaterialTheme.colors.background)
            .fillMaxWidth()
            .height(64.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { screen ->

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onItemClick(screen.id) }
                    .padding(16.dp, 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                MyAppIcon(resourceId = screen.icon)
                Text(screen.title, fontSize = 11.sp)
            }
        }
    }
}

@Composable
@Preview(widthDp = 393)
fun P1() {
    BottomNavigation {}
}
