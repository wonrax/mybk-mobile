package com.wonrax.mybk

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.wonrax.mybk.models.DeviceUser
import com.wonrax.mybk.ui.MybkUI
import com.wonrax.mybk.viewmodels.SchedulesViewModel

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
        }

        setContent {
            MybkUI(schedulesViewModel)
        }
    }
}

@Composable
fun Greeting(name: String?) {
    Text(text = "Hello $name!")
}
