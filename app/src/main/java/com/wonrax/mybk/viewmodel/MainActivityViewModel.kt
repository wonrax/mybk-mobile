package com.wonrax.mybk.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.wonrax.mybk.LoginActivity
import com.wonrax.mybk.model.DeviceUser
import com.wonrax.mybk.model.SSOState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {
    var isInitiated = false
    val isLoading = mutableStateOf(true)
    var schedulesViewModel: SchedulesViewModel? = null

    fun constructor(context: Activity) {

        isInitiated = true

        if (DeviceUser.username == null || DeviceUser.password == null) {
            startActivity(context, Intent(context, LoginActivity::class.java), null)
            context.finish()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            // Try sign in
            val ssoStatus = DeviceUser.signIn()
            if (ssoStatus != SSOState.LOGGED_IN) {
                startActivity(context, Intent(context, LoginActivity::class.java), null)
                context.finish()
                return@launch
            }
            DeviceUser.getMybkToken()
            isLoading.value = false

            // Init screen viewmodels here
            schedulesViewModel = ViewModelProvider(context as ViewModelStoreOwner)[SchedulesViewModel::class.java]
        }
    }
}
