package com.wonrax.bkstinfo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.wonrax.bkstinfo.ui.theme.BKSTINFOTheme
import com.wonrax.bkstinfo.utils.Cookuest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BKSTINFOTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }

        signIn()
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

private fun signIn() {
    val url = "https://sso.hcmut.edu.vn/cas/login"
    Cookuest.get(url)
}
