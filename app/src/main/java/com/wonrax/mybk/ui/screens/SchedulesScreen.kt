package com.wonrax.mybk.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wonrax.mybk.ui.component.DropdownMenu
import com.wonrax.mybk.ui.component.FontSize
import com.wonrax.mybk.ui.component.FontWeight
import com.wonrax.mybk.ui.component.MainScreenLayout
import com.wonrax.mybk.ui.component.ScheduleCard
import com.wonrax.mybk.ui.component.Text
import com.wonrax.mybk.ui.theme.Color
import com.wonrax.mybk.viewmodel.MybkViewModel
import java.util.Calendar
import java.util.Date

@Composable
fun SchedulesScreen(mybkViewModel: MybkViewModel) {
    MainScreenLayout(
        isLoading = mybkViewModel.isLoading.value,
        isRefreshing = mybkViewModel.isRefreshing.value,
        onRefresh = { mybkViewModel.update() }
    ) {
        item {
            Column(
                modifier = Modifier.padding(12.dp, 0.dp)
            ) {
                Text(
                    "Giờ học",
                    fontWeight = FontWeight.Medium,
                    fontSize = FontSize.Heading,
                )

                // TODO make this a function
                // TODO make this a state so it doesn't get updated every recomposition
                                /* Build a calendar suitable to extract ISO8601 week numbers
                             * (see http://en.wikipedia.org/wiki/ISO_8601_week_number) */
                val calendar: Calendar = Calendar.getInstance()
                calendar.minimalDaysInFirstWeek = 4
                calendar.firstDayOfWeek = Calendar.MONDAY

                /* Set date */
                calendar.time = Date()

                /* Get ISO8601 week number */
                val w = calendar.get(Calendar.WEEK_OF_YEAR)

                Text("Tuần $w", color = Color.Grey50)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (mybkViewModel.schedulesData.value != null) {
            item {
                DropdownMenu(
                    items = mybkViewModel.schedulesData.value!!,
                    itemToStringRepresentation = { item -> item.ten_hocky }, // TODO Shorten this string
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
                ScheduleCard(schedule)
            }
        }
    }
}
