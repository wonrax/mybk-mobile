package com.wonrax.mybk.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wonrax.mybk.ui.component.DropdownMenu
import com.wonrax.mybk.ui.component.ExamCard
import com.wonrax.mybk.ui.component.FontSize
import com.wonrax.mybk.ui.component.FontWeight
import com.wonrax.mybk.ui.component.LastUpdated
import com.wonrax.mybk.ui.component.MainScreenLayout
import com.wonrax.mybk.ui.component.Text
import com.wonrax.mybk.ui.theme.Color
import com.wonrax.mybk.viewmodel.MybkViewModel

@Composable
fun ExamsScreen(mybkViewModel: MybkViewModel) {
    MainScreenLayout(
        isLoading = mybkViewModel.isLoading.value,
        isRefreshing = mybkViewModel.isRefreshing.value,
        onRefresh = { mybkViewModel.update() }
    ) {
        item {
            Text(
                "Lịch thi",
                fontWeight = FontWeight.Medium,
                fontSize = FontSize.Heading,
                modifier = Modifier.padding(12.dp, 0.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (mybkViewModel.examsData.value != null) {
            item {
                DropdownMenu(
                    items = mybkViewModel.examsData.value!!,
                    itemToStringRepresentation = { item -> item.ten_hocky },
                    selectedItem = mybkViewModel.selectedExamSemester.value,
                    onSelectItem = {
                            // TODO bring this up to viewmodel
                            item ->
                        mybkViewModel.selectedExamSemester.value = item
                    }
                )
            }
        } else {
            item {
                Text(
                    text = "Không có dữ liệu.",
                    fontSize = FontSize.Large,
                    color = Color.Grey50,
                    modifier = Modifier.padding(12.dp, 0.dp)
                )
            }
        }

        mybkViewModel.selectedExamSemester.value?.lichthi?.forEach {
            item {
                ExamCard(courseExam = it)
            }
        }

        item {
            LastUpdated(
                mybkViewModel.selectedExamSemester.value?.ngay_cap_nhat ?: "",
                "MM/dd/yyyy HH:mm:ss aa"
            )
        }
    }
}
