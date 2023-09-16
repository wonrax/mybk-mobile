package com.wonrax.mybk.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.wonrax.mybk.ui.component.LoadingIcon
import com.wonrax.mybk.ui.theme.Color

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .background(Color.Grey10)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LoadingIcon()
    }
}
