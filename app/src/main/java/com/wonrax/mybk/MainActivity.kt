package com.wonrax.mybk

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.wonrax.mybk.models.DeviceUser
import com.wonrax.mybk.ui.MybkUI

class MainActivity : ComponentActivity() {
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
            MybkUI()
        }

//        CoroutineScope(Dispatchers.IO).launch {
//            DeviceUser.signIn()
//            val status = DeviceUser.getMybkToken()
//            val token = DeviceUser.stinfoToken
//
//            val body: RequestBody = FormBody.Builder().apply {
//                if (token != null) {
//                    add("_token", token)
//                }
//            }.build()
//
//            val scheduleResponse = Cookuest.post(
//                "https://mybk.hcmut.edu.vn/stinfo/lichthi/ajax_lichhoc",
//                body
//            ).await()
//
//            CoroutineScope(Dispatchers.Main).launch {
//                if (status == MybkState.LOGGED_IN) {
//                    setContent {
//                        MybkUI {
//                            Greeting(scheduleResponse.body)
//                        }
//                    }
//                }
//            }
//        }
    }
}

@Composable
fun Greeting(name: String?) {
    Text(text = "Hello $name!")
}
