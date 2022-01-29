package com.wonrax.bkstinfo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.wonrax.bkstinfo.models.DeviceUser
import com.wonrax.bkstinfo.models.MybkState
import com.wonrax.bkstinfo.network.Cookuest
import com.wonrax.bkstinfo.network.await
import com.wonrax.bkstinfo.ui.theme.BKSTINFOTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.RequestBody

class MainActivity : ComponentActivity() {
    private fun setMainTheme() {
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.BKSTINFO_NoActionBar)

        if (DeviceUser.username == null || DeviceUser.password == null) {

            startActivity(Intent(this, LoginActivity::class.java))
            finish()

            // If we don't have return here, the code below continues to execute even when we're
            // in another activity
            return
        }

        setContent {
            BKSTINFOTheme(false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Hello, please wait")
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
                        BKSTINFOTheme(false) {
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
    BKSTINFOTheme {
        Greeting("Android")
    }
}
