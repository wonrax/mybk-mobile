package com.wonrax.mybk.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.wonrax.mybk.ui.component.FontSize
import com.wonrax.mybk.ui.component.FontWeight
import com.wonrax.mybk.ui.component.Icon
import com.wonrax.mybk.ui.component.Icons
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
                Modifier.height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    icon = Icons.ArrowLeft,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(16.dp, 0.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = false),
                        ) { if (upAction != null) upAction() },
                    tint = Color.Primary
                )
                Text(
                    title,
                    color = Color.Primary,
                    fontSize = FontSize.Large,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(0.dp, 16.dp),
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
