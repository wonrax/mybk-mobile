package com.wonrax.mybk.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wonrax.mybk.ui.component.FontSize
import com.wonrax.mybk.ui.component.ScheduleCard
import com.wonrax.mybk.ui.component.Text
import com.wonrax.mybk.ui.theme.Color
import com.wonrax.mybk.viewmodel.SchedulesViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SchedulesScreen(schedulesViewModel: SchedulesViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (schedulesViewModel.isLoading.value) {
                // TODO make this a component
                CircularProgressIndicator(
                    color = Color.Primary,
                    strokeWidth = 1.5.dp
                )
                Text(text = "Đang tải dữ liệu, chờ xí...")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(12.dp, 64.dp)
                ) {
                    item {
                        Text("Giờ học", fontSize = FontSize.Heading)
                        val w = SimpleDateFormat("w", Locale("vn")).format(Date())
                        Text("Tuần $w", color = Color.Grey50)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    schedulesViewModel.response.value?.forEach { semester ->
                        semester.tkb?.forEach { schedule ->
                            item {
                                ScheduleCard(schedule)
                            }
                        }
                    }
                }
            }
        }
    }
}
