package com.wonrax.bkstinfo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.wonrax.bkstinfo.models.DeviceUser
import com.wonrax.bkstinfo.models.LoginStatus
import com.wonrax.bkstinfo.ui.theme.BKSTINFOTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BKSTINFOTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting() {
    var displayLoginStatus by remember { mutableStateOf("Hellow, pls login") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
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
                DeviceUser().signIn(username, password) { loginStatus: LoginStatus ->
                    displayLoginStatus =
                        when (loginStatus) {
                            LoginStatus.WRONG_PASSWORD -> "Wrong password, please try again"
                            LoginStatus.LOGGED_IN -> "Login successfully"
                            LoginStatus.UNKNOWN -> "Something went wrong on our side, please try again later"
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
