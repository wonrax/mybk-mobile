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
import java.net.UnknownHostException

class SnackBarState(
    val isShowingSnackbar: Boolean,
    val message: String? = null,
    val onAction: (() -> Unit)? = null
)

class MainActivityViewModel : ViewModel() {
    private var isInitiated = false
    val isLoading = mutableStateOf(true)
    lateinit var mybkViewModel: MybkViewModel
    var snackBarState = mutableStateOf(SnackBarState(false))

    fun constructor(context: Activity) {
        if (isInitiated) return
        isInitiated = true

        if (DeviceUser.username == null || DeviceUser.password == null) {
            startActivity(context, Intent(context, LoginActivity::class.java), null)
            context.finish()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            // Try sign in
            try {
                val ssoStatus = DeviceUser.signIn()
                if (ssoStatus != SSOState.LOGGED_IN) {
                    startActivity(context, Intent(context, LoginActivity::class.java), null)
                    context.finish()
                    return@launch
                }
                DeviceUser.getMybkToken()
            } catch (e: UnknownHostException) {
                snackBarState.value = SnackBarState(
                    true,
                    "Không thể kết nối. Đang hiển thị dữ liệu cũ."
                )
            } catch (e: Exception) {
                snackBarState.value = SnackBarState(
                    true,
                    e.message
                )
            }
            // Init screen viewmodels here
            mybkViewModel = ViewModelProvider(context as ViewModelStoreOwner)[MybkViewModel::class.java]
            mybkViewModel.constructor(context, snackBarState)

            isLoading.value = false
        }
    }

    fun dismissSnackBar() {
        snackBarState.value = SnackBarState(false)
    }
}
