package com.wonrax.mybk.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.wonrax.mybk.ui.component.BottomNavigation
import com.wonrax.mybk.ui.theme.MybkTheme

@Composable
fun MybkUI(content: @Composable () -> Unit) {
    MybkTheme(false) {
        Scaffold(
            bottomBar = { BottomNavigation() }
        ) {

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.LightGray,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    content()
                }
            }
        }
    }
}
