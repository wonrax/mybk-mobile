package com.wonrax.mybk

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.wonrax.mybk.model.DeviceUser
import com.wonrax.mybk.model.SSOState
import com.wonrax.mybk.ui.MybkUI
import com.wonrax.mybk.ui.screens.LoadingScreen
import com.wonrax.mybk.viewmodel.SchedulesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val schedulesViewModel: SchedulesViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Mybk_NoActionBar)

        if (DeviceUser.username == null || DeviceUser.password == null) {

            startActivity(Intent(this, LoginActivity::class.java))
            finish()

            // If we don't have return here, the code below continues to execute even when we're
            // in another activity
            return
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                setContent {
                    LoadingScreen()
                }
                // Try sign in
                val ssoStatus = DeviceUser.signIn()
                if (ssoStatus != SSOState.LOGGED_IN) {
                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                    finish()
                }
                DeviceUser.getMybkToken()
                setContent {
                    MybkUI(schedulesViewModel)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String?) {
    Text(text = "Hello $name!")
}
