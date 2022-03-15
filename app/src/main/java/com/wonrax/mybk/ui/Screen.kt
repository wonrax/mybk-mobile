package com.wonrax.mybk.ui

import com.wonrax.mybk.ui.component.Icons

sealed class Screen(
    val route: String,
    val title: String,
    val icon: Icons,
    val iconSelected: Icons,
) {
    object Schedules : Screen("home/schedules", "Giờ học", Icons.TimeCircle, Icons.TimeCircleBold)
    object Exams : Screen("home/exams", "Lịch thi", Icons.Calendar, Icons.CalendarBold)
    object Transcript : Screen("home/transcript", "Bảng điểm", Icons.Transcript, Icons.TranscriptBold)
    object Profile : Screen("home/profile", "Cá nhân", Icons.Profile, Icons.ProfileBold)

    object Items {
        val list = listOf(
            Schedules,
            Exams,
            Transcript,
            Profile
        )
    }
}
