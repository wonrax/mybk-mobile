package com.wonrax.mybk.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wonrax.mybk.model.schedule.CourseSchedule
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
                .padding(16.dp, 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (courseSchedule != null) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
            } else {
                Text("Không tìm thấy môn học. Vui lòng thử làm mới dữ liệu.")
            }
        }
    }
}
