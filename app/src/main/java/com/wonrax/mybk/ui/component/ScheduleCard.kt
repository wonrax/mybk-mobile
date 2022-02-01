package com.wonrax.mybk.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.wonrax.mybk.ui.theme.MybkColors
import com.wonrax.mybk.viewmodels.CourseSchedule

@Composable
fun ScheduleCard(schedule: CourseSchedule) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(MybkColors.Light)
            .padding(24.dp)
            .fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            schedule.ten_mh?.let { Text(it) }
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
