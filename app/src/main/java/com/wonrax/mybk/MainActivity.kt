package com.wonrax.mybk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.wonrax.mybk.ui.MybkUI
import com.wonrax.mybk.ui.screens.LoadingScreen
import com.wonrax.mybk.viewmodel.MainActivityViewModel

class MainActivity : ComponentActivity() {
    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Mybk_NoActionBar)

        if (!mainActivityViewModel.isInitiated)
            mainActivityViewModel.constructor(this)

        setContent {
            if (mainActivityViewModel.isLoading.value)
                LoadingScreen()
            else
                mainActivityViewModel.schedulesViewModel?.let { MybkUI(it) }
        }
    }
}

// TODO Remove this when done
@Composable
fun Greeting(name: String?) {
    Text(text = "Hello $name!")
}
