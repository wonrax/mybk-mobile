package com.wonrax.mybk.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.wonrax.mybk.ui.component.FontSize
import com.wonrax.mybk.ui.component.FontWeight
import com.wonrax.mybk.ui.component.Text
import com.wonrax.mybk.ui.theme.Color

@Composable
fun TitleBarScreen(
    title: String,
    upAction: (() -> Unit)? = null,
    content: (@Composable () -> Unit)? = null
) {
    val systemUIController = rememberSystemUiController()
    LaunchedEffect(true) {
        systemUIController.setStatusBarColor(Color.Light, darkIcons = true)
    }

    val scrollState = rememberScrollState()

    Column() {
        Surface(
            Modifier.fillMaxWidth(),
            color = Color.Light,
            elevation = 8.dp
        ) {
            Row(
                Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "<",
                    color = Color.Primary,
                    fontSize = FontSize.Large,
                    modifier = Modifier.clickable { if (upAction != null) upAction() }
                )
                Text(
                    title,
                    color = Color.Primary,
                    fontSize = FontSize.Large,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        Column(
            Modifier
                .background(Color.Grey10)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            if (content != null) {
                content()
            }
        }
    }
}
