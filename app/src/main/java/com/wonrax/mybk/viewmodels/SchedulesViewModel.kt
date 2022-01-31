package com.wonrax.mybk.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.wonrax.mybk.models.DeviceUser
import com.wonrax.mybk.models.MybkState
import com.wonrax.mybk.network.Cookuest
import com.wonrax.mybk.network.await
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.RequestBody
import org.apache.commons.lang.StringEscapeUtils

class SchedulesViewModel : ViewModel() {
    val response = mutableStateOf("")
    val isLoading = mutableStateOf(true)

    init {
        // TODO first fetch here
    }

    private fun changeResponse(value: String) {
        response.value = value
    }

    fun update() {
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

            val decoded = StringEscapeUtils.unescapeJava(scheduleResponse.body)

            println("debug: $decoded")

            CoroutineScope(Dispatchers.Main).launch {
                if (status == MybkState.LOGGED_IN) {
                    changeResponse(decoded)
                    isLoading.value = false
                }
            }
        }
    }
}
