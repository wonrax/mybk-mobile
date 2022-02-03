package com.wonrax.mybk.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
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
            .clip(RoundedCornerShape(24.dp))
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
            schedule.thu1?.let { Text("Thứ $it") }
            if (schedule.giobd != null && schedule.giokt != null) {
                Text("${schedule.giobd} - ${schedule.giokt}")
            }
            schedule.phong1?.let { Text("Phòng $it") }
            schedule.macoso?.let { Text("Cơ sở $it") }
            schedule.tuan_hoc?.let { Text("Tuần học: $it") }
        }
    }
}

@Preview
@Composable
fun Preview() {
    val mockCourse = CourseSchedule(
        giobd = "7:00",
        giokt = "8:50",
        hk_nh = "20212",
        ma_mh = "CO69420",
        ma_nhom = "L01",
        macoso = "BK-CS1",
        mssv = "1812069",
        ngay_cap_nhat = "2022-01-25T18:56:24.000+0000",
        nhomto = "L01",
        phong1 = "HANGOUT_MEET",
        so_tin_chi = 3,
        tc_hp = 3f,
        ten_hocky = "Học kỳ 2 Năm học 2021 - 2022",
        ten_mh = "DE CUONG LUAN VAN",
        thu1 = 3,
        tiet_bd1 = 2,
        tiet_kt1 = 3,
        tuan_hoc = "01|02|03|--|--|06|07|08|09|--|--|--|--|14|15|16|17|18|"
    )
    ScheduleCard(schedule = mockCourse)
}
