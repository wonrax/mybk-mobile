package com.wonrax.mybk.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wonrax.mybk.ui.theme.Color

@Composable
fun Divider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Grey30)
            .height(1.dp)
    )
}
