package com.wonrax.bkstinfo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.wonrax.bkstinfo.models.DeviceUser
import com.wonrax.bkstinfo.models.SSOState
import com.wonrax.bkstinfo.ui.theme.BKSTINFOTheme
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.BKSTINFO_NoActionBar)
        setContent {
            BKSTINFOTheme(false) {
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
    var displayLoginStatus by remember { mutableStateOf("Hellow, pls login") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current as Activity
    val composableScope = rememberCoroutineScope()

    Column {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            onClick = {
                composableScope.launch {

                    val loginStatus = DeviceUser.signIn(username, password)

                    if (loginStatus == SSOState.LOGGED_IN) {
                        context.startActivity(Intent(context, MainActivity::class.java))

                        // TODO: Investigate if this returns immediately or continue to execute
                        // code below
                        context.finish()
                    }

                    displayLoginStatus =
                        when (loginStatus) {
                            SSOState.WRONG_PASSWORD -> "Wrong password, please try again"
                            SSOState.LOGGED_IN -> {
                                DeviceUser.getMybkToken()
                                "Login successfully"
                            }
                            SSOState.UNKNOWN -> "Something went wrong on our side, please try again"
                            SSOState.TOO_MANY_TRIES ->
                                "The CAS has blocked you temporarily because you've performed " +
                                    "too many failed login attempts! Please wait at least 5 " +
                                    "minutes before retrying. Check your password carefully!"
                            else -> displayLoginStatus
                        }
                }
            },
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = Color.White
            )
        ) {
            Text("Sign in")
        }
        Text(displayLoginStatus)
    }
}
