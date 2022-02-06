package com.wonrax.mybk

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.wonrax.mybk.model.DeviceUser
import com.wonrax.mybk.model.SSOState
import com.wonrax.mybk.ui.component.FontSize
import com.wonrax.mybk.ui.component.FontWeight
import com.wonrax.mybk.ui.component.ScreenLayout
import com.wonrax.mybk.ui.component.Text
import com.wonrax.mybk.ui.theme.Color
import com.wonrax.mybk.ui.theme.MybkTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setTheme(R.style.Mybk_NoActionBar)
        setContent {
            MybkTheme(false) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    LoginForm()
                }
            }
        }
    }
}

@Composable
fun LoginForm() {
    var displayLoginStatus by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current as Activity
    val composableScope = rememberCoroutineScope()

    ScreenLayout {
        MybkTheme {
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
                            modifier = Modifier.padding(12.dp, 0.dp)
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
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = username,
                                onValueChange = { username = it },
                                placeholder = { Text("Tên đăng nhập", color = Color.Grey50) },
                                shape = RoundedCornerShape(24.dp),
                                singleLine = true,
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = Color.Light,
                                    cursorColor = Color.Primary,
                                    disabledLabelColor = Color.Grey30,
                                    focusedIndicatorColor = Color.Primary,
                                    unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                                    unfocusedLabelColor = androidx.compose.ui.graphics.Color.Transparent
                                ),
                            )
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = password,
                                onValueChange = { password = it },
                                placeholder = { Text("Mật khẩu", color = Color.Grey50) },
                                shape = RoundedCornerShape(24.dp),
                                singleLine = true,
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = Color.Light,
                                    cursorColor = Color.Primary,
                                    disabledLabelColor = Color.Grey30,
                                    focusedIndicatorColor = Color.Primary,
                                    unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                                    unfocusedLabelColor = androidx.compose.ui.graphics.Color.Transparent
                                ),
                                visualTransformation = PasswordVisualTransformation()
                            )
                        }
                        if (displayLoginStatus != "")
                            Box(modifier = Modifier.padding(12.dp, 0.dp)) {
                                Text(displayLoginStatus)
                            }
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable {
                                        displayLoginStatus = ""
                                        composableScope.launch(Dispatchers.IO) {

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

                                            displayLoginStatus =
                                                when (loginStatus) {
                                                    SSOState.WRONG_PASSWORD -> "Sai tên đăng nhập hoặc mật khẩu."
                                                    SSOState.UNKNOWN -> "Lỗi bất ngờ, vui lòng thử lại sau."
                                                    SSOState.TOO_MANY_TRIES ->
                                                        "Bạn đã bị hệ thống chặn tạm thời vì vượt quá số lần thử. Vui lòng đợi ít nhất 3 phút trước khi thử lại."
                                                    else -> displayLoginStatus
                                                }
                                        }
                                    }
                                    .background(Color.Primary)
                                    .padding(24.dp, 12.dp)
                            ) {
                                Text(
                                    "Đăng nhập",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Light
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
