package com.wonrax.mybk.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.wonrax.mybk.ui.component.FontSize
import com.wonrax.mybk.ui.component.Icon
import com.wonrax.mybk.ui.component.Icons
import com.wonrax.mybk.ui.component.ScheduleCard
import com.wonrax.mybk.ui.component.Text
import com.wonrax.mybk.ui.theme.Color
import com.wonrax.mybk.viewmodel.SchedulesViewModel
import java.util.Calendar
import java.util.Date

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
                    strokeWidth = 2.dp
                )
                Text(text = "Đang tải dữ liệu, chờ tí...")
            } else {
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
                                fontSize = FontSize.Heading,
                            )

                            // TODO make this a function
                            /* Build a calendar suitable to extract ISO8601 week numbers
                             * (see http://en.wikipedia.org/wiki/ISO_8601_week_number) */
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

                    item {
                        dropDownMenu()
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

@Composable
fun dropDownMenu() {

    var expanded by remember { mutableStateOf(false) }
    val suggestions = listOf("Kotlin", "Java", "Dart", "Python")
    var selectedText by remember { mutableStateOf("Not selected") }

    var textfieldSize by remember { mutableStateOf(Size.Zero) }

    Column {
        Surface(
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    // This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()
                }
                .clip(RoundedCornerShape(32.dp))
                .clickable { expanded = !expanded }
                .background(Color.Light)
                .padding(24.dp, 12.dp)
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(selectedText)

                // TODO animate this if possible
                if (expanded) Icon(Icons.ArrowUp) else Icon(Icons.ArrowDown)
            }
        }
        MaterialTheme(shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(24.dp))) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { textfieldSize.width.toDp() }),
            ) {
                suggestions.forEach { label ->
                    DropdownMenuItem(onClick = {
                        selectedText = label
                        expanded = false
                    }) {
                        Text(text = label)
                    }
                }
            }
        }
    }
}
