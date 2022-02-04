package com.wonrax.mybk.ui.component

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.wonrax.mybk.ui.theme.Color

@Composable
fun LoadingIcon() {
    CircularProgressIndicator(
        color = Color.Primary,
        strokeWidth = 2.dp
    )
}
