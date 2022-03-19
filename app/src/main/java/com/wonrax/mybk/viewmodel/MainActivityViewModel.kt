package com.wonrax.mybk.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import com.wonrax.mybk.LoginActivity
import com.wonrax.mybk.model.DeviceUser
import com.wonrax.mybk.model.SSOState
import com.wonrax.mybk.model.SnackbarManager
import com.wonrax.mybk.model.UserSettings
import com.wonrax.mybk.network.OkHttpClientSingleton
import com.wonrax.mybk.repository.ExamsRepository
import com.wonrax.mybk.repository.GradesRepository
import com.wonrax.mybk.repository.SchedulesRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class MainActivityViewModel : ViewModel() {
    private var isInitiated = false
    lateinit var mybkViewModel: MybkViewModel
    private val snackbarManager = SnackbarManager
    var logOut: (() -> Unit)? = null

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        CoroutineScope(Dispatchers.Main).launch {
            mybkViewModel.isLoading.value = false
            mybkViewModel.isRefreshing.value = false
            when (exception) {
                is UnknownHostException -> {
                    snackbarManager.showMessage("Không thể kết nối. Đang hiển thị dữ liệu cũ.")
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

    private fun _logOut(context: Activity, mybkViewModel: MybkViewModel) {
        // Delete cookies
        OkHttpClientSingleton.cookieJar.clear()

        // Invalidate local storage
        mybkViewModel.invalidateLocalStorage()
        DeviceUser.clear()
        UserSettings.clear()

        // Start login page
        startActivity(
            context,
            Intent(context, LoginActivity::class.java),
            null
        )
        context.finish()
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

        logOut = { _logOut(context, mybkViewModel) }

        if (UserSettings.updateWhenStartUp.value || !mybkViewModel.isDataCached)
            CoroutineScope(Dispatchers.IO).launch(coroutineExceptionHandler) {
                when (DeviceUser.signIn()) {
                    SSOState.LOGGED_IN -> {
                        DeviceUser.getMybkToken()
                        mybkViewModel.update()
                    }
                    SSOState.TOO_MANY_TRIES -> {
                        withContext(Dispatchers.Main) {
                            snackbarManager.showMessage("Bạn đang tạm thời bị chặn. Vui lòng đợi ít nhất 1 phút trước khi thử lại.")
                        }
                        mybkViewModel.isLoading.value = false
                        mybkViewModel.isRefreshing.value = false
                    }
                    SSOState.UNKNOWN -> {
                        withContext(Dispatchers.Main) {
                            snackbarManager.showMessage("Không thể đăng nhập vào tài khoản. Đang hiển thị dữ liệu cũ.")
                        }
                        mybkViewModel.isLoading.value = false
                        mybkViewModel.isRefreshing.value = false
                    }
                    else -> {
                        _logOut(context, mybkViewModel)
                        return@launch
                    }
                }
            }
        else {
            mybkViewModel.isLoading.value = false
            mybkViewModel.isRefreshing.value = false
        }
    }
}
