package com.wonrax.mybk.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wonrax.mybk.model.schedule.CourseSchedule
import com.wonrax.mybk.ui.theme.Color

@Composable
fun ScheduleCard(schedule: CourseSchedule, onCourseClick: (semester: String, courseId: String) -> Unit) {
    CourseCardLayout(
        courseName = schedule.ten_mh,
        courseId = schedule.ma_mh,
        courseClassNumber = schedule.ma_nhom,
        courseCredit = schedule.so_tin_chi,
        onCourseClick = {
            schedule.hk_nh?.let {
                schedule.ma_mh?.let { it1 ->
                    onCourseClick(
                        it,
                        it1
                    )
                }
            }
        }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Course start & end time
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.TimeCircle)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        schedule.thu1?.let { Text("Thứ $it") }
                        if (schedule.giobd != null && schedule.giokt != null) {
                            Text("${schedule.giobd} - ${schedule.giokt}", color = Color.Primary)
                        }
                    }
                }

                // Course location
                schedule.phong1?.let {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Location)
                        Text(schedule.phong1)
                    }
                }
            }

            schedule.tuan_hoc?.let { _ ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Tuần")
                    AvailableWeeksScrollable(schedule.tuan_hoc, Color.Light)
                }
            }
        }
    }
}

@Composable
fun AvailableWeeksScrollable(
    tuan_hoc: String,
    fadeColor: androidx.compose.ui.graphics.Color = Color.Light
) {
    Box(
        contentAlignment = Alignment.CenterEnd
    ) {
        val weekScrollState = rememberScrollState()
        Row(
            Modifier.horizontalScroll(weekScrollState),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tuan_hoc.split("|").forEach { it ->
                if (it != "") {
                    val week = it.toIntOrNull()
                    if (week != null) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.Primary10)
                                .widthIn(min = 36.dp)
                                .padding(6.dp, 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(String.format("%02d", week), color = Color.Primary)
                        }
                    } else {
                        Text(
                            "--",
                            color = Color.Grey30
                        )
                    }
                }
            }
            // Compensate for the fading edge
            Spacer(modifier = Modifier.width(24.dp))
        }

        // Fading edge to indicate scrollable
        Box(
            Modifier
                .size(48.dp, 36.dp)
                .drawWithContent {
                    val colors = listOf(fadeColor, Color.Transparent)
                    drawContent()
                    drawRect(
                        brush = Brush.horizontalGradient(
                            colors,
                            startX = Float.POSITIVE_INFINITY,
                            endX = 0f
                        ),
                    )
                }
        )
    }
}

@Preview
@Composable
fun Preview() {
    val mockCourse = CourseSchedule(
        giobd = "7:00",
        giokt = "8:50",
//        hk_nh = "20212",
        ma_mh = "CO69420",
//        ma_nhom = "L01",
        macoso = "BK-CS1",
//        mssv = "1812069",
//        ngay_cap_nhat = "2022-01-25T18:56:24.000+0000",
        nhomto = "L01",
        phong1 = "HANGOUT_MEET",
        so_tin_chi = 3,
//        tc_hp = 3f,
//        ten_hocky = "Học kỳ 2 Năm học 2021 - 2022",
        ten_mh = "DE CUONG LUAN VAN TOT NGHIEP KHOA HOC MAY TINH",
        thu1 = 3,
//        tiet_bd1 = 2,
//        tiet_kt1 = 3,
        tuan_hoc = "01|02|03|--|--|06|07|08|09|--|--|--|--|14|15|16|17|18|"
//        tuan_hoc = "--|"
    )
    ScheduleCard(schedule = mockCourse) { _, _ -> }
}
