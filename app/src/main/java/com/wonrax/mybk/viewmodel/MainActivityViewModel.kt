package com.wonrax.mybk.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import com.wonrax.mybk.LoginActivity
import com.wonrax.mybk.model.DeviceUser
import com.wonrax.mybk.model.SSOState
import com.wonrax.mybk.model.SnackbarManager
import com.wonrax.mybk.repository.ExamsRepository
import com.wonrax.mybk.repository.GradesRepository
import com.wonrax.mybk.repository.SchedulesRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class MainActivityViewModel : ViewModel() {
    private var isInitiated = false
    lateinit var mybkViewModel: MybkViewModel
    private val snackbarManager = SnackbarManager

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        CoroutineScope(Dispatchers.Main).launch {
            mybkViewModel.isLoading.value = false
            mybkViewModel.isRefreshing.value = false
            when (exception) {
                is UnknownHostException -> {
                    snackbarManager.showMessage("Không thể kết nối. Đang hiển thị dữ liệu cũ.")
                    snackbarManager.showMessage("Queued")
                }
                is SocketTimeoutException -> {
                    snackbarManager.showMessage("Không thể kết nối. Đang hiển thị dữ liệu cũ.")
                }
                else -> {
                    snackbarManager.showMessage(exception.localizedMessage ?: "Lỗi không xác định")
                }
            }
        }
    }

    fun constructor(context: Activity) {
        if (isInitiated) return
        isInitiated = true

        if (DeviceUser.username == null || DeviceUser.password == null) {
            startActivity(context, Intent(context, LoginActivity::class.java), null)
            context.finish()
            return
        }

        // Init screen viewmodels here
        mybkViewModel = MybkViewModel(
            SchedulesRepository(context.filesDir),
            ExamsRepository(context.filesDir),
            GradesRepository(context.filesDir),
            SnackbarManager
        )

        CoroutineScope(Dispatchers.IO).launch(coroutineExceptionHandler) {
            // Try sign in
            val ssoStatus = DeviceUser.signIn()

            // TODO handle too many tries, unknown cases etc.
            if (ssoStatus != SSOState.LOGGED_IN) {
                startActivity(context, Intent(context, LoginActivity::class.java), null)
                context.finish()
                return@launch
            }
            DeviceUser.getMybkToken()

            mybkViewModel.update()
        }
    }
}
