package com.wonrax.mybk

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.wonrax.mybk.model.DeviceUser
import com.wonrax.mybk.model.SSOState
import com.wonrax.mybk.ui.component.FontSize
import com.wonrax.mybk.ui.component.FontWeight
import com.wonrax.mybk.ui.component.ScreenLayout
import com.wonrax.mybk.ui.component.Text
import com.wonrax.mybk.ui.component.TextField
import com.wonrax.mybk.ui.screens.PolicyScreen
import com.wonrax.mybk.ui.slideInLeft
import com.wonrax.mybk.ui.slideInRight
import com.wonrax.mybk.ui.slideOutLeft
import com.wonrax.mybk.ui.slideOutRight
import com.wonrax.mybk.ui.theme.Color
import com.wonrax.mybk.ui.theme.MybkTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class LoginActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Mybk_NoActionBar)
        setContent {
            MybkTheme(false) {
                LoginNavigation(navController = rememberAnimatedNavController(), UserCredential())
            }
        }
    }
}

// Act as a mini viewmodel to save data between navigations
class UserCredential(
    val username: MutableState<String> = mutableStateOf(""),
    val password: MutableState<String> = mutableStateOf("")
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoginNavigation(navController: NavHostController, userCredential: UserCredential) {
    AnimatedNavHost(navController = navController, startDestination = "login") {
        composable(
            "login",
            popEnterTransition = { slideInRight() },
            exitTransition = { slideOutLeft() }
        ) {
            ScreenLayout {
                LoginScreen(userCredential) {
                    navController.navigate("policy") {
                        launchSingleTop = true
                    }
                }
            }
        }
        composable(
            "policy",
            enterTransition = { slideInLeft() },
            popExitTransition = { slideOutRight() }
        ) {
            PolicyScreen(upAction = navController::popBackStack)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(userCredential: UserCredential, navigateToPolicyScreen: () -> Unit) {
    val displayLoginStatus = remember { mutableStateOf("") }
    val isLoading = remember { mutableStateOf(false) }
    val context = LocalContext.current as Activity
    val composableScope = rememberCoroutineScope()

    val localFocusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // Change status bar to grey when come back from other screens
    val systemUIController = rememberSystemUiController()
    LaunchedEffect(true) {
        systemUIController.setStatusBarColor(Color.Grey10, darkIcons = true)
    }

    val onLogin = {
        composableScope.launch(Dispatchers.IO) {
            try {
                isLoading.value = true
                val loginStatus = DeviceUser.signIn(
                    userCredential.username.value,
                    userCredential.password.value
                )

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

                isLoading.value = false
                displayLoginStatus.value =
                    when (loginStatus) {
                        SSOState.WRONG_PASSWORD -> "Sai tên đăng nhập hoặc mật khẩu."
                        SSOState.UNKNOWN -> "Lỗi bất ngờ, vui lòng thử lại sau."
                        SSOState.TOO_MANY_TRIES ->
                            "Bạn đã bị hệ thống chặn tạm thời vì vượt quá số lần thử. Vui lòng đợi ít nhất 1 phút trước khi thử lại."
                        else -> displayLoginStatus.value
                    }
            } catch (e: UnknownHostException) {
                displayLoginStatus.value = "Không thể kết nối, vui lòng kiểm tra đường truyền."
            } catch (e: Exception) {
                displayLoginStatus.value = "Lỗi: ${e.localizedMessage}"
            } finally {
                isLoading.value = false
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
                        userCredential.username.value,
                        imeAction = ImeAction.Next,
                        keyboardActions = KeyboardActions(
                            onNext = { localFocusManager.moveFocus(FocusDirection.Down) }
                        )
                    ) { userCredential.username.value = it }
                    TextField(
                        "Mật khẩu",
                        userCredential.password.value,
                        imeAction = ImeAction.Go,
                        keyboardActions = KeyboardActions(
                            onGo = { keyboardController?.hide(); onLogin() },
                        ),
                        password = true
                    ) { userCredential.password.value = it }
                }
                AnimatedVisibility(
                    displayLoginStatus.value != "" && !isLoading.value,
                    enter = slideIn { IntOffset(0, -10) },
                    exit = slideOut { IntOffset(0, -10) }
                ) {
                    Box(modifier = Modifier.padding(12.dp, 0.dp)) {
                        Text(displayLoginStatus.value)
                    }
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Điều khoản sử dụng",
                        color = Color.Primary,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = navigateToPolicyScreen
                            )
                            .padding(8.dp)
                    )
                    val buttonColor = if (!isLoading.value) Color.Primary else Color.Grey50
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { if (!isLoading.value) onLogin() }
                            .background(buttonColor)
                            .padding(24.dp, 12.dp)
                    ) {
                        val buttonText = if (isLoading.value) "Đang đăng nhập..." else "Đăng nhập"
                        Text(
                            buttonText,
                            fontWeight = FontWeight.Medium,
                            color = Color.Light
                        )
                    }
                }
            }
        }
    }
}
