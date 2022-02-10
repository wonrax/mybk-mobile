package com.wonrax.mybk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.wonrax.mybk.ui.MybkUI
import com.wonrax.mybk.viewmodel.MainActivityViewModel

class MainActivity : ComponentActivity() {
    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Mybk_NoActionBar)

        mainActivityViewModel.constructor(this)

        setContent {
            MybkUI(mainActivityViewModel)
        }
    }
}
