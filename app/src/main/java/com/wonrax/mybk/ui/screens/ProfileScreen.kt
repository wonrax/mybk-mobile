package com.wonrax.mybk.ui.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.wonrax.mybk.APP_VERSION
import com.wonrax.mybk.LoginActivity
import com.wonrax.mybk.WEBSITE_URL
import com.wonrax.mybk.model.DeviceUser
import com.wonrax.mybk.ui.component.FontSize
import com.wonrax.mybk.ui.component.FontWeight
import com.wonrax.mybk.ui.component.ScreenLayout
import com.wonrax.mybk.ui.component.Text
import com.wonrax.mybk.ui.component.TextLink
import com.wonrax.mybk.ui.theme.Color

@Composable
fun ProfileScreen(navigateToPolicyScreen: () -> Unit) {

    // Change status bar to grey when come back from other screens
    val systemUIController = rememberSystemUiController()
    LaunchedEffect(true) {
        systemUIController.setStatusBarColor(Color.Grey10, darkIcons = true)
    }

    val context = LocalContext.current as Activity
    ScreenLayout {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(8.dp, 72.dp)
        ) {
            item {
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
                    DeviceUser.username?.let { Text(it, fontWeight = FontWeight.Bold) }
                    DeviceUser.faculty?.let { Text(it) }
                }
            }

            item {
                // TODO make this a button component
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            // TODO move this to viewmodel
                            DeviceUser.signOut(context)
                            ContextCompat.startActivity(
                                context,
                                Intent(context, LoginActivity::class.java),
                                null
                            )
                            context.finish()
                        }
                        .background(Color.Light)
                        .padding(24.dp, 12.dp)
                ) {
                    Text("Đăng xuất", fontWeight = FontWeight.Bold, color = Color.Error)
                }
            }
            item {
                Spacer(modifier = Modifier.height(36.dp))
                Column(
                    modifier = Modifier.padding(12.dp, 0.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Về Mybk Mobile", fontSize = FontSize.Large, fontWeight = FontWeight.Medium)
                    Text("Phiên bản $APP_VERSION", color = Color.Grey50)
                    Text("Điều khoản sử dụng", Modifier.clickable { navigateToPolicyScreen() })
                    TextLink(context, "Mybk Mobile trên GitHub", WEBSITE_URL)
                }
            }
        }
    }
}
