package com.wonrax.mybk.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.wonrax.mybk.ui.screens.LoadingScreen
import com.wonrax.mybk.ui.theme.Color

@Composable
fun MainScreenLayout(
    isLoading: Boolean,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    content: LazyListScope.() -> Unit
) {
    // Change status bar to grey when come back from other screens
    val systemUIController = rememberSystemUiController()
    LaunchedEffect(true) {
        systemUIController.setStatusBarColor(Color.Grey10, darkIcons = true)
    }

    if (isLoading) {
        LoadingScreen()
    } else {
        ScreenLayout {
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = { onRefresh() },
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(12.dp, 72.dp)
                ) {
                    content()

                    // Pad the bottom navigation
                    item { Spacer(Modifier.height(36.dp)) }
                }
            }
        }
    }
}
