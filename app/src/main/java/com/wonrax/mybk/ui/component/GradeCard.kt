package com.wonrax.mybk.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.wonrax.mybk.model.grade.CourseGrade
import com.wonrax.mybk.ui.theme.Color

@Composable
fun GradeCard(courseGrade: CourseGrade) {
    CourseCardLayout(
        courseName = courseGrade.ten_mh,
        courseId = courseGrade.ma_mh,
        courseClassNumber = courseGrade.nhomto,
        courseCredit = courseGrade.so_tin_chi
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            courseGrade.diem_thanhphan?.let {
                GradeSection(title = "Điểm thành phần") { Text(it) }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                courseGrade.diem_thi?.let {
                    GradeSection(title = "Điểm thi") {
                        Text(it, fontSize = FontSize.Large)
                    }
                }

                courseGrade.diem_tong_ket?.let {
                    GradeSection(title = "Điểm tổng kết") {
                        Text(
                            it,
                            fontSize = FontSize.Large,
                            fontWeight = FontWeight.Bold,
                            color = Color.Primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GradeSection(title: String, grade: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(title, fontWeight = FontWeight.Medium)
        grade()
    }
}
