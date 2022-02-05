package com.wonrax.mybk.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wonrax.mybk.ui.component.FontSize
import com.wonrax.mybk.ui.component.FontWeight
import com.wonrax.mybk.ui.component.MainScreenLayout
import com.wonrax.mybk.ui.component.Text
import com.wonrax.mybk.viewmodel.SchedulesViewModel

@Composable
fun ExamsScreen(examsViewModel: SchedulesViewModel) {
    MainScreenLayout(
        isLoading = examsViewModel.isLoading.value,
        isRefreshing = examsViewModel.isRefreshing.value,
        onRefresh = { examsViewModel.update() }
    ) {
        item {
            Column(
                modifier = Modifier.padding(12.dp, 0.dp)
            ) {
                Text(
                    "Lịch thi",
                    fontWeight = FontWeight.Medium,
                    fontSize = FontSize.Heading,
                )
            }
        }
    }
}
