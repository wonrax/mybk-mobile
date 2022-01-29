package com.wonrax.mybk

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.wonrax.mybk.models.DeviceUser
import com.wonrax.mybk.models.MybkState
import com.wonrax.mybk.network.Cookuest
import com.wonrax.mybk.network.await
import com.wonrax.mybk.ui.theme.MybkTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.RequestBody

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
            MybkTheme(false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            DeviceUser.signIn()
            val status = DeviceUser.getMybkToken()
            val token = DeviceUser.stinfoToken

            val body: RequestBody = FormBody.Builder().apply {
                if (token != null) {
                    add("_token", token)
                }
            }.build()

            val scheduleResponse = Cookuest.post(
                "https://mybk.hcmut.edu.vn/stinfo/lichthi/ajax_lichhoc",
                body
            ).await()

            CoroutineScope(Dispatchers.Main).launch {
                if (status == MybkState.LOGGED_IN) {
                    setContent {
                        MybkTheme(false) {
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = MaterialTheme.colors.background
                            ) {
                                Greeting(scheduleResponse.body)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String?) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MybkTheme {
        Greeting("Android")
    }
}
