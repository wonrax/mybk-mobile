package com.wonrax.mybk.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wonrax.mybk.model.schedule.CourseSchedule
import com.wonrax.mybk.ui.theme.Color

@Composable
fun ScheduleCard(schedule: CourseSchedule) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(32.dp))
            .background(Color.Light)
            .padding(24.dp)
            .fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            schedule.ten_mh?.let {
                Text(
                    it,
                    fontSize = FontSize.Large,
                    fontWeight = FontWeight.Bold,
                    color = Color.Dark
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
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
                        schedule.thu1?.let { Text("Thứ $it".uppercase(), fontWeight = FontWeight.Bold) }
                        if (schedule.giobd != null && schedule.giokt != null) {
                            Text("${schedule.giobd} - ${schedule.giokt}", color = Color.Primary)
                        }
                    }
                }

                // Course location
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Location)
                    Column {
                        schedule.phong1?.let { Text(it, fontWeight = FontWeight.Bold) }
                        schedule.macoso?.let { Text(it) }
                    }
                }
            }

            schedule.tuan_hoc?.let { it ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Tuần")
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        it.split("|").forEach { it1 ->
                            if (it1 != "") {
                                val week = it1.toIntOrNull()
                                item {
                                    Text(
                                        if (week != null) String.format("%02d", week) else "--",
                                        color = if (week != null) Color.Dark else Color.Grey30,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    val mockCourse = CourseSchedule(
        giobd = "7:00",
        giokt = "8:50",
//        hk_nh = "20212",
//        ma_mh = "CO69420",
//        ma_nhom = "L01",
        macoso = "BK-CS1",
//        mssv = "1812069",
//        ngay_cap_nhat = "2022-01-25T18:56:24.000+0000",
//        nhomto = "L01",
        phong1 = "HANGOUT_MEET",
//        so_tin_chi = 3,
//        tc_hp = 3f,
//        ten_hocky = "Học kỳ 2 Năm học 2021 - 2022",
        ten_mh = "DE CUONG LUAN VAN TOT NGHIEP KHOA HOC MAY TINH",
        thu1 = 3,
//        tiet_bd1 = 2,
//        tiet_kt1 = 3,
        tuan_hoc = "01|02|03|--|--|06|07|08|09|--|--|--|--|14|15|16|17|18|"
//        tuan_hoc = "--|"
    )
    ScheduleCard(schedule = mockCourse)
}
