package com.wonrax.mybk.ui.screens

import android.app.Activity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.wonrax.mybk.APP_VERSION
import com.wonrax.mybk.WEBSITE_URL
import com.wonrax.mybk.model.DeviceUser
import com.wonrax.mybk.model.UserSettings
import com.wonrax.mybk.ui.component.FontSize
import com.wonrax.mybk.ui.component.FontWeight
import com.wonrax.mybk.ui.component.Icon
import com.wonrax.mybk.ui.component.Icons
import com.wonrax.mybk.ui.component.ScreenLayout
import com.wonrax.mybk.ui.component.Text
import com.wonrax.mybk.ui.theme.Color
import com.wonrax.mybk.viewmodel.MainActivityViewModel
import com.wonrax.mybk.viewmodel.openBrowser
import kotlinx.coroutines.flow.update

@Composable
fun ProfileScreen(
    mainActivityViewModel: MainActivityViewModel,
    navigateToPolicyScreen: () -> Unit,
    navigateToFeedback: () -> Unit
) {
    // Change status bar to grey when come back from other screens
    val systemUIController = rememberSystemUiController()
    LaunchedEffect(true) {
        systemUIController.setStatusBarColor(Color.Grey10, darkIcons = true)
    }

    val context = LocalContext.current as Activity
    ScreenLayout {
        Box(Modifier.verticalScroll(rememberScrollState())) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp, 72.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp, 0.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DeviceUser.fullName?.let {
                        Text(
                            it,
                            fontSize = FontSize.Heading,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        DeviceUser.username?.let { Text(it, color = Color.Primary) }
                        DeviceUser.faculty?.let { Text(it) }
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val updateWhenStartUpSetting = UserSettings.updateWhenStartUp.collectAsState()
                    Toggle(
                        text = "Tự động cập nhật dữ liệu",
                        description = "Tắt tính năng này để ứng dụng không tự cập nhật dữ liệu mỗi khi khởi động. Bạn vẫn có thể vuốt xuống để cập nhật dữ liệu.",
                        defaultState = updateWhenStartUpSetting.value
                    ) { newValue ->
                        UserSettings.updateWhenStartUp.update { newValue }
                    }
                    FullWidthButton(
                        text = "Điều khoản sử dụng",
                        icon = Icons.Info,
                        onClick = navigateToPolicyScreen
                    )
                    FullWidthButton(
                        text = "Mybk Mobile trên GitHub",
                        icon = Icons.ExternalLink,
                        onClick = { openBrowser(context, WEBSITE_URL) }
                    )
                    FullWidthButton(
                        text = "Liên hệ / Phản hồi",
                        icon = Icons.Message,
                        onClick = navigateToFeedback
                    )
                    FullWidthButton(
                        text = "Đăng xuất",
                        icon = Icons.Logout,
                        color = Color.Error,
                        onClick = {
                            mainActivityViewModel.logOut?.let { it() }
                        }
                    )
                    FullWidthButton(
                        text = "Phiên bản $APP_VERSION",
                        disabled = true
                    )
                }
                // Pad the bottom navigation
                Spacer(Modifier.height(36.dp))
            }
        }
    }
}

@Composable
fun FullWidthButton(
    text: String,
    icon: Icons? = null,
    disabled: Boolean = false,
    color: androidx.compose.ui.graphics.Color? = null,
    onClick: (() -> Unit)? = null
) {
    var modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(32.dp))
    if (onClick != null) modifier = modifier.then(Modifier.clickable(onClick = onClick))

    var contentColor = color ?: Color.Dark
    if (disabled) contentColor = Color.Grey50

    Row(
        modifier.then(
            Modifier
                .background(if (!disabled) Color.Light else Color.Transparent)
                .padding(16.dp)
        ),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(icon, tint = contentColor)
        }
        Text(text, fontWeight = FontWeight.Medium, color = contentColor)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Toggle(
    text: String,
    description: String? = null,
    defaultState: Boolean,
    onToggle: ((Boolean) -> Unit)
) {
    val isEnabled = remember { mutableStateOf(defaultState) }

    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(Color.Light)
            .clickable {
                onToggle(!isEnabled.value)
                isEnabled.value = !isEnabled.value
            }
            .padding(20.dp, 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text, fontWeight = FontWeight.Medium)
            description?.let { Text(it, fontSize = FontSize.Small, color = Color.Grey50) }
        }
        AnimatedContent(
            targetState = isEnabled.value
        ) { isEnabledState ->
            if (isEnabledState) {
                Icon(Icons.ToggleEnabled)
            } else {
                Icon(Icons.ToggleDisabled)
            }
        }
    }
}
