package com.wonrax.mybk.ui

import com.wonrax.mybk.ui.component.Icons

// TODO include composable for each screen
sealed class Screen(
    val id: String,
    val title: String,
    val icon: Icons,
    val iconSelected: Icons,
) {
    //    object Home : Screen("home", "Trang chủ", R.drawable.ic_home)
    object Schedules : Screen("schedules", "Giờ học", Icons.TimeCircle, Icons.TimeCircleBold)
    object Exams : Screen("exams", "Lịch thi", Icons.Calendar, Icons.CalendarBold)
    object Transcript : Screen("transcript", "Bảng điểm", Icons.Transcript, Icons.TranscriptBold)
    object Profile : Screen("profile", "Cá nhân", Icons.Profile, Icons.ProfileBold)

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
