package com.wonrax.mybk.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wonrax.mybk.ui.component.FontSize
import com.wonrax.mybk.ui.component.FontWeight
import com.wonrax.mybk.ui.component.Text
import com.wonrax.mybk.ui.theme.Color

val messages = listOf(
    "Hello, cám ơn bạn đã sử dụng app, hiện tại mình không nhận phản hồi qua tin nhắn hay email.",
    "Bạn vui lòng mở issue trên GitHub nếu có yêu cầu thêm hay thay đổi tính năng."
)

@Composable
fun FeedbackDialog(upAction: () -> Unit) {
    Card(
        shape = RoundedCornerShape(32.dp),
        backgroundColor = Color.Light
    ) {
        Column() {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Phản hồi",
                    fontSize = FontSize.Large,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                messages.forEach {
                    Text(
                        text = it,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(Color.Primary10)
                    .clickable(onClick = upAction)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "OK", fontWeight = FontWeight.Medium, color = Color.Primary)
            }
        }
    }
}
