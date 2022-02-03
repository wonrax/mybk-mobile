package com.wonrax.mybk.ui

import com.wonrax.mybk.R

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
