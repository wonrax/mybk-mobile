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
import com.wonrax.bkstinfo.ui.theme.BKSTINFOTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.BKSTINFO_NoActionBar)

        if (DeviceUser.getUsername() == null || DeviceUser.getPassword() == null) {
            println("debug: not logged in")

            startActivity(Intent(this, LoginActivity::class.java))
            finish()

            return
        }

        setContent {
            BKSTINFOTheme(false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Hehe")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BKSTINFOTheme {
        Greeting("Android")
    }
}
