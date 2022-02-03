package com.wonrax.mybk.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.wonrax.mybk.model.DeviceUser
import com.wonrax.mybk.model.MybkState
import com.wonrax.mybk.model.schedule.SemesterSchedule
import com.wonrax.mybk.network.Cookuest
import com.wonrax.mybk.network.await
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.RequestBody

class SchedulesViewModel : ViewModel() {
    val response = mutableStateOf<Array<SemesterSchedule>?>(null)

    val isLoading = mutableStateOf(true)

    init {
        update()
    }

    private fun changeResponse(value: Array<SemesterSchedule>) {
        response.value = value
    }

    private fun update() {
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

            val deserializedResponse: Array<SemesterSchedule> = Gson().fromJson(scheduleResponse.body, Array<SemesterSchedule>::class.java)

            if (status == MybkState.LOGGED_IN) {
                changeResponse(deserializedResponse)
                isLoading.value = false
            }
        }
    }
}
