package com.wonrax.mybk.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.wonrax.mybk.ui.component.DropdownMenu
import com.wonrax.mybk.ui.component.FontSize
import com.wonrax.mybk.ui.component.FontWeight
import com.wonrax.mybk.ui.component.ScheduleCard
import com.wonrax.mybk.ui.component.Text
import com.wonrax.mybk.ui.theme.Color
import com.wonrax.mybk.viewmodel.SchedulesViewModel
import java.util.Calendar
import java.util.Date

@Composable
fun SchedulesScreen(schedulesViewModel: SchedulesViewModel) {
    if (schedulesViewModel.isLoading.value) {
        // TODO make this a component
//        LoadingIcon()
//        Text(text = "Đang tải dữ liệu, chờ tí...")
        LoadingScreen()
    } else {
        ScreenLayout {
            SwipeRefresh(
                state = rememberSwipeRefreshState(schedulesViewModel.isRefreshing.value),
                onRefresh = { schedulesViewModel.update() },
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(12.dp, 72.dp)
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

                    if (schedulesViewModel.response.value != null) {
                        item {
                            DropdownMenu(
                                items = schedulesViewModel.response.value!!,
                                itemToStringRepresentation = { item -> item.ten_hocky },
                                selectedItem = schedulesViewModel.selectedSemester.value,
                                onSelectItem = {
                                    // TODO bring this up to viewmodel
                                    item ->
                                    schedulesViewModel.selectedSemester.value = item
                                }
                            )
                        }
                    }
                    schedulesViewModel.selectedSemester.value?.tkb?.forEach { schedule ->
                        item {
                            ScheduleCard(schedule)
                        }
                    }
                }
            }
        }
    }
}
