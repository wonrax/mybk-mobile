package com.wonrax.mybk.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wonrax.mybk.ui.theme.Color

@Composable
fun Divider(
    color: androidx.compose.ui.graphics.Color = Color.Grey20,
    width: Dp = 1.dp
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color)
            .height(width)
    )
}
