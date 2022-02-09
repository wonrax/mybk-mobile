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
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.ripple.rememberRipple
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.wonrax.mybk.model.DeviceUser
import com.wonrax.mybk.model.SSOState
import com.wonrax.mybk.ui.component.FontSize
import com.wonrax.mybk.ui.component.FontWeight
import com.wonrax.mybk.ui.component.Icon
import com.wonrax.mybk.ui.component.Icons
import com.wonrax.mybk.ui.component.ScreenLayout
import com.wonrax.mybk.ui.component.Text
import com.wonrax.mybk.ui.component.TextLink
import com.wonrax.mybk.ui.theme.Color
import com.wonrax.mybk.ui.theme.MybkTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

@Composable
fun TextField(
    placeHolder: String,
    value: String,
    modifier: Modifier = Modifier,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    imeAction: ImeAction = ImeAction.Default,
    password: Boolean = false,
    onValueChange: (String) -> Unit
) {
    val mod = modifier.fillMaxWidth()
    var showPassword by remember { mutableStateOf(false) }
    val visualTransformation =
        if (password) {
            if (!showPassword)
                PasswordVisualTransformation()
            else VisualTransformation.None
        } else VisualTransformation.None

    OutlinedTextField(
        modifier = mod,
        value = value,
        onValueChange = { onValueChange(it) },
        placeholder = { Text(placeHolder, color = Color.Grey50) },
        shape = RoundedCornerShape(24.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = imeAction),
        keyboardActions = keyboardActions,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Light,
            cursorColor = Color.Primary,
            disabledLabelColor = Color.Grey30,
            focusedIndicatorColor = Color.Primary,
            unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
            unfocusedLabelColor = androidx.compose.ui.graphics.Color.Transparent
        ),
        visualTransformation = visualTransformation,
        trailingIcon = {
            if (password)
                Icon(
                    Icons.EyeOff,
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = false),
                        ) {
                            showPassword = !showPassword
                        }
                )
        }
    )
}
