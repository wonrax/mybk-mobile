package com.wonrax.mybk.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wonrax.mybk.ui.component.DropdownMenu
import com.wonrax.mybk.ui.component.FontSize
import com.wonrax.mybk.ui.component.FontWeight
import com.wonrax.mybk.ui.component.LastUpdated
import com.wonrax.mybk.ui.component.MainScreenLayout
import com.wonrax.mybk.ui.component.ScheduleCard
import com.wonrax.mybk.ui.component.Text
import com.wonrax.mybk.viewmodel.MybkViewModel

@Composable
fun SchedulesScreen(
    mybkViewModel: MybkViewModel,
    onCourseClick: (semester: String, courseId: String) -> Unit
) {
    MainScreenLayout(
        isLoading = mybkViewModel.isLoading.value,
        isRefreshing = mybkViewModel.isRefreshing.value,
        onRefresh = { mybkViewModel.update() }
    ) {
        item {
            Text(
                "Thời khóa biểu",
                fontWeight = FontWeight.Medium,
                fontSize = FontSize.Heading,
                modifier = Modifier.padding(12.dp, 0.dp)
            )
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

        mybkViewModel.selectedScheduleSemester.value?.tkb?.forEach { schedule ->
            item {
                ScheduleCard(schedule, onCourseClick)
            }
        }

        item {
            LastUpdated(
                mybkViewModel.selectedScheduleSemester.value?.ngay_cap_nhat ?: "",
                "yyyy-MM-dd HH:mm:ss"
            )
        }
    }
}
