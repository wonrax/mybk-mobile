package com.wonrax.mybk.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wonrax.mybk.ui.component.DropdownMenu
import com.wonrax.mybk.ui.component.FontSize
import com.wonrax.mybk.ui.component.FontWeight
import com.wonrax.mybk.ui.component.LastUpdated
import com.wonrax.mybk.ui.component.MainScreenLayout
import com.wonrax.mybk.ui.component.ScheduleCard
import com.wonrax.mybk.ui.component.Text
import com.wonrax.mybk.ui.theme.Color
import com.wonrax.mybk.viewmodel.MybkViewModel
import java.util.Calendar
import java.util.Date

@Composable
fun SchedulesScreen(
    mybkViewModel: MybkViewModel,
    onCourseClick: (semester: String, courseId: String) -> Unit
) {
    val weekOfYear by remember { mutableStateOf(getWeekOfYear()) }

    MainScreenLayout(
        isLoading = mybkViewModel.isLoading.value,
        isRefreshing = mybkViewModel.isRefreshing.value,
        onRefresh = { mybkViewModel.update() }
    ) {
        item {
            Row(
                modifier = Modifier
                    .padding(12.dp, 0.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Giờ học",
                    fontWeight = FontWeight.Medium,
                    fontSize = FontSize.Heading,
                )

                Text("Tuần $weekOfYear", color = Color.Grey50)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (mybkViewModel.schedulesData.value != null) {
            item {
                DropdownMenu(
                    items = mybkViewModel.schedulesData.value!!,
                    itemToStringRepresentation = { item -> item.ten_hocky },
                    selectedItem = mybkViewModel.selectedScheduleSemester.value,
                    onSelectItem = {
                        // TODO bring this up to viewmodel
                        item ->
                        mybkViewModel.selectedScheduleSemester.value = item
                    }
                )
            }
        }

        item {
            LastUpdated(
                mybkViewModel.selectedScheduleSemester.value?.ngay_cap_nhat ?: "",
                "yyyy-MM-dd HH:mm:ss"
            )
        }

        mybkViewModel.selectedScheduleSemester.value?.tkb?.forEach { schedule ->
            item {
                ScheduleCard(schedule, onCourseClick)
            }
        }
    }
}

fun getWeekOfYear(): Int {
    /* Build a calendar suitable to extract ISO8601 week numbers
     * (see http://en.wikipedia.org/wiki/ISO_8601_week_number) */
    val calendar: Calendar = Calendar.getInstance()
    calendar.minimalDaysInFirstWeek = 4
    calendar.firstDayOfWeek = Calendar.MONDAY

    /* Set date */
    calendar.time = Date()

    /* Get ISO8601 week number */
    return calendar.get(Calendar.WEEK_OF_YEAR)
}
