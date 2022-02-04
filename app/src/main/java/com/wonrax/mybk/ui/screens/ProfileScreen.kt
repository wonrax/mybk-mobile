package com.wonrax.mybk.ui.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.wonrax.mybk.LoginActivity
import com.wonrax.mybk.model.DeviceUser
import com.wonrax.mybk.ui.component.FontSize
import com.wonrax.mybk.ui.component.FontWeight
import com.wonrax.mybk.ui.component.Text
import com.wonrax.mybk.ui.theme.Color

@Composable
fun ProfileScreen() {
    val context = LocalContext.current as Activity
    Surface(
        modifier = Modifier
            .background(Color.Grey10)
            .fillMaxSize(),
        color = Color.Grey10
    ) {
        Column(
            modifier = Modifier.padding(12.dp, 72.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp, 0.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DeviceUser.fullName?.let {
                    Text(
                        it,
                        fontSize = FontSize.Heading,
                    )
                }
                DeviceUser.username?.let { Text(it, fontWeight = FontWeight.Bold) }
                DeviceUser.faculty?.let { Text(it) }
            }
            // TODO make this a button component
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        DeviceUser.signOut()
                        startActivity(context, Intent(context, LoginActivity::class.java), null)
                        context.finish()
                    }
                    .background(Color.Light)
                    .padding(24.dp, 12.dp)
            ) {
                Text("Đăng xuất", fontWeight = FontWeight.Bold, color = Color.Error)
            }
        }
    }
}
