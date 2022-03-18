package com.wonrax.mybk.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wonrax.mybk.model.schedule.CourseSchedule
import com.wonrax.mybk.ui.component.AvailableWeeksScrollable
import com.wonrax.mybk.ui.component.Divider
import com.wonrax.mybk.ui.component.FontSize
import com.wonrax.mybk.ui.component.FontWeight
import com.wonrax.mybk.ui.component.Text
import com.wonrax.mybk.ui.theme.Color
import java.util.Locale

@Composable
fun CourseDetailScreen(courseSchedule: CourseSchedule?, upAction: (() -> Unit)?) {
    TitleBarScreen(title = courseSchedule?.ma_mh ?: "", upAction = upAction) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (courseSchedule != null) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Môn học".uppercase(Locale.getDefault()),
                            fontSize = FontSize.Body,
                            fontWeight = FontWeight.Medium,
                            color = Color.Grey50
                        )
                        Text(
                            text = courseSchedule.ten_mh ?: "",
                            fontSize = FontSize.Heading,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                CourseDetail(
                    courseSchedule.giobd,
                    courseSchedule.giokt,
                    courseSchedule.ma_mh,
                    courseSchedule.ma_nhom,
                    courseSchedule.macoso,
                    courseSchedule.phong1,
                    courseSchedule.so_tin_chi,
                    courseSchedule.tc_hp,
                    courseSchedule.ten_hocky,
                    courseSchedule.thu1,
                    courseSchedule.tiet_bd1,
                    courseSchedule.tiet_kt1,
                    courseSchedule.tuan_hoc
                )
            } else {
                Text("Không tìm thấy môn học. Vui lòng thử làm mới dữ liệu.")
            }
        }
    }
}

val fields = listOf(
    "Mã môn học",
    "Tiết học",
    "Thứ",
    "Giờ học",
    "Mã cơ sở",
    "Phòng",
    "Mã nhóm",
    "Số tín chỉ",
    "Tín chỉ học phần",
    "Học kỳ"
)

@Composable
fun CourseDetail(
    giobd: String?,
    giokt: String?,
    ma_mh: String?,
    ma_nhom: String?,
    macoso: String?,
    phong1: String?,
    so_tin_chi: Int?,
    tc_hp: Float?,
    ten_hocky: String?,
    thu1: Int?,
    tiet_bd1: Int?,
    tiet_kt1: Int?,
    tuan_hoc: String?
) {
    val tietHoc = if (tiet_bd1 != null && tiet_kt1 != null) "$tiet_bd1 - $tiet_kt1" else null
    val gioHoc = if (giobd != null && giokt != null) "$giobd - $giokt" else null

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOfNotNull(
            ma_mh,
            tietHoc,
            thu1.toString(),
            gioHoc,
            macoso,
            phong1,
            ma_nhom,
            so_tin_chi.toString(),
            tc_hp.toString(),
            ten_hocky
        )
            .forEachIndexed { i, item ->
                if (i != 0) {
                    Divider()
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(fields[i] + ":")
                    Text(item, color = Color.Primary)
                }
            }

        if (tuan_hoc != null) {
            Divider()
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Tuần:")
                AvailableWeeksScrollable(tuan_hoc, Color.Grey10)
            }
        }
    }
}
