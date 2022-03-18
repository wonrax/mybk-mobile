package com.wonrax.mybk.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.wonrax.mybk.model.exam.CourseExam

@Composable
fun ExamCard(courseExam: CourseExam) {
    CourseCardLayout(
        courseName = courseExam.ten_mh,
        courseClassNumber = courseExam.nhomto,
        courseId = courseExam.ma_mh,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ExamSchedule(
                title = "Kiểm tra giữa kỳ",
                date = courseExam.ngaykt,
                time = courseExam.gio_kt,
                location = courseExam.phong_ktra
            )
            ExamSchedule(
                title = "Kiểm tra cuối kỳ",
                date = courseExam.ngaythi,
                time = courseExam.gio_thi,
                location = courseExam.phong_thi
            )
        }
    }
}

@Composable
fun IconedText(icon: Icons, text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon)
        Text(text, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun ExamSchedule(title: String, date: String?, time: String?, location: String?) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(title, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            if (date != null) IconedText(icon = Icons.Calendar, text = date)
            if (time != null) IconedText(icon = Icons.TimeCircle, text = time)
            if (location != null) IconedText(icon = Icons.Location, text = location)
        }
    }
}
