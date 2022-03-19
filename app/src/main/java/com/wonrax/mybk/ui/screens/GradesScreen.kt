package com.wonrax.mybk.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wonrax.mybk.ui.component.Divider
import com.wonrax.mybk.ui.component.DropdownMenu
import com.wonrax.mybk.ui.component.FontSize
import com.wonrax.mybk.ui.component.FontWeight
import com.wonrax.mybk.ui.component.GradeCard
import com.wonrax.mybk.ui.component.LastUpdated
import com.wonrax.mybk.ui.component.MainScreenLayout
import com.wonrax.mybk.ui.component.Text
import com.wonrax.mybk.ui.theme.Color
import com.wonrax.mybk.viewmodel.MybkViewModel

@Composable
fun GradesScreen(mybkViewModel: MybkViewModel) {
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
                    "Bảng điểm",
                    fontWeight = FontWeight.Medium,
                    fontSize = FontSize.Heading,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (mybkViewModel.gradesData.value != null) {
            item {
                DropdownMenu(
                    items = mybkViewModel.gradesData.value!!,
                    itemToStringRepresentation = { item -> item.ten_hocky },
                    selectedItem = mybkViewModel.selectedGradeSemester.value,
                    onSelectItem = {
                        // TODO bring this up to viewmodel
                        item ->
                        mybkViewModel.selectedGradeSemester.value = item
                    }
                )
            }

            if (listOfNotNull( // Avoid redundant padding gap when the list is empty
                    mybkViewModel.selectedGradeSemester.value?.so_tc,
                    mybkViewModel.selectedGradeSemester.value?.so_tctl_hk,
                    mybkViewModel.selectedGradeSemester.value?.diem_tb,
                    mybkViewModel.selectedGradeSemester.value?.so_tctl,
                    mybkViewModel.selectedGradeSemester.value?.diem_tbtl
                ).isNotEmpty()
            ) {
                item {
                    SemesterSummary(
                        so_tc = mybkViewModel.selectedGradeSemester.value?.so_tc,
                        so_tctl_hk = mybkViewModel.selectedGradeSemester.value?.so_tctl_hk,
                        diem_tb = mybkViewModel.selectedGradeSemester.value?.diem_tb,
                        so_tctl = mybkViewModel.selectedGradeSemester.value?.so_tctl,
                        diem_tbtl = mybkViewModel.selectedGradeSemester.value?.diem_tbtl
                    )
                }
            }
        }

        mybkViewModel.selectedGradeSemester.value?.diem?.forEach {
            item {
                GradeCard(it)
            }
        }

        item {
            LastUpdated(
                mybkViewModel.selectedGradeSemester.value?.ngay_cap_nhat ?: "",
                "dd/MM/yyyy hh:mm:ss aa"
            )
        }
    }
}

val summaryTitles = listOf<String>(
    "Số tín chỉ đăng ký học kỳ",
    "Số tín chỉ tích lũy học kỳ",
    "Điểm trung bình học kỳ",
    "Số tín chỉ tích lũy",
    "Điểm trung bình tích lũy"
)

@Composable
fun SemesterSummary(
    so_tc: String?,
    so_tctl_hk: String?,
    diem_tb: String?,
    so_tctl: String?,
    diem_tbtl: String?
) {
    val isEmpty = listOfNotNull(so_tc, so_tctl_hk, diem_tb, so_tctl, diem_tbtl).isEmpty()

    if (!isEmpty) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp, 0.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(so_tc, so_tctl_hk, diem_tb, so_tctl, diem_tbtl)
                    .forEachIndexed() { index, item ->
                        item?.let {
                            if (index != 0) {
                                Divider()
                            }
                            SemesterSummaryRow(title = summaryTitles[index], value = item)
                        }
                    }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SemesterSummaryRow(title: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, fontWeight = FontWeight.Medium)
        Text(value, fontWeight = FontWeight.Medium, color = Color.Primary)
    }
}
