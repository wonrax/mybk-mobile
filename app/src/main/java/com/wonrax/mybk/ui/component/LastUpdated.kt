package com.wonrax.mybk.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wonrax.mybk.ui.theme.Color
import com.wonrax.mybk.viewmodel.formatDateToDisplay

@Composable
fun LastUpdated(
    dateString: String,
    dateFormat: String = "yyyy-MM-dd HH:mm:ss"
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(12.dp, 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        var displayDate: String? = formatDateToDisplay(
            dateString,
            dateFormat
        )

        if (displayDate != null) {
            Text(text = "Cập nhật:", color = Color.Grey50)
            Text(text = displayDate.toString(), color = Color.Grey50)
        }
    }
}
