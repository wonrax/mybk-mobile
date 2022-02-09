package com.wonrax.mybk

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.wonrax.mybk.model.DeviceUser
import com.wonrax.mybk.model.SSOState
import com.wonrax.mybk.ui.component.FontSize
import com.wonrax.mybk.ui.component.FontWeight
import com.wonrax.mybk.ui.component.ScreenLayout
import com.wonrax.mybk.ui.component.Text
import com.wonrax.mybk.ui.component.TextField
import com.wonrax.mybk.ui.component.TextLink
import com.wonrax.mybk.ui.theme.Color
import com.wonrax.mybk.ui.theme.MybkTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Mybk_NoActionBar)
        setContent {
            MybkTheme(false) {
                ScreenLayout {
                    LoginScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen() {
    var displayLoginStatus by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current as Activity
    val composableScope = rememberCoroutineScope()

    val localFocusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val onLogin = {
        composableScope.launch(Dispatchers.IO) {
            try {
                isLoading = true
                val loginStatus = DeviceUser.signIn(username, password)

                if (loginStatus == SSOState.LOGGED_IN) {
                    context.startActivity(
                        Intent(
                            context,
                            MainActivity::class.java
                        )
                    )

                    context.finish()
                    return@launch
                }

                isLoading = false
                displayLoginStatus =
                    when (loginStatus) {
                        SSOState.WRONG_PASSWORD -> "Sai tên đăng nhập hoặc mật khẩu."
                        SSOState.UNKNOWN -> "Lỗi bất ngờ, vui lòng thử lại sau."
                        SSOState.TOO_MANY_TRIES ->
                            "Bạn đã bị hệ thống chặn tạm thời vì vượt quá số lần thử. Vui lòng đợi ít nhất 3 phút trước khi thử lại."
                        else -> displayLoginStatus
                    }
            } catch (e: UnknownHostException) {
                displayLoginStatus = "Không thể kết nối, vui lòng kiểm tra đường truyền."
            } catch (e: Exception) {
                displayLoginStatus = "Lỗi: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(12.dp, 64.dp)

    ) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .padding(12.dp, 0.dp)
                ) {
                    Text(
                        "Đăng nhập sử dụng tài khoản Mybk",
                        fontWeight = FontWeight.Medium,
                        fontSize = FontSize.Heading
                    )
                    Text(
                        "Ứng dụng dành cho sinh viên Trường Đại học Bách Khoa - ĐHQG TP.HCM",
                        color = Color.Grey50
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    TextField(
                        "Tên đăng nhập",
                        username,
                        imeAction = ImeAction.Next,
                        keyboardActions = KeyboardActions(
                            onNext = { localFocusManager.moveFocus(FocusDirection.Down) }
                        )
                    ) { username = it }
                    TextField(
                        "Mật khẩu",
                        password,
                        imeAction = ImeAction.Go,
                        keyboardActions = KeyboardActions(
                            onGo = { keyboardController?.hide(); onLogin() },
                        ),
                        password = true
                    ) { password = it }
                }
                AnimatedVisibility(displayLoginStatus != "" && !isLoading) {
                    Box(modifier = Modifier.padding(12.dp, 0.dp)) {
                        Text(displayLoginStatus)
                    }
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextLink(context = context, title = "Điều khoản sử dụng", url = POLICIES_URL)
                    val buttonColor = if (!isLoading) Color.Primary else Color.Grey50
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { if (!isLoading) onLogin() }
                            .background(buttonColor)
                            .padding(24.dp, 12.dp)
                    ) {
                        val buttonText = if (isLoading) "Đang đăng nhập..." else "Đăng nhập"
                        Text(
                            buttonText,
                            fontWeight = FontWeight.Bold,
                            color = Color.Light
                        )
                    }
                }
            }
        }
    }
}
