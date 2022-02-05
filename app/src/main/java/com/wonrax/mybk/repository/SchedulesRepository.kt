package com.wonrax.mybk.repository

import androidx.compose.runtime.mutableStateOf
import com.google.gson.Gson
import com.wonrax.mybk.model.DeviceUser
import com.wonrax.mybk.model.schedule.SemesterSchedule
import com.wonrax.mybk.network.Cookuest
import com.wonrax.mybk.network.Response
import com.wonrax.mybk.network.await
import okhttp3.FormBody
import okhttp3.RequestBody
import kotlin.reflect.KSuspendFunction1

class SchedulesRepository {
    var data = mutableStateOf<Array<SemesterSchedule>?>(null)

    suspend fun getRemote() {
        val scheduleResponse = tryRequest(::requestSchedule)

        if (scheduleResponse != null) {
            val deserializedResponse: Array<SemesterSchedule> =
                Gson().fromJson(scheduleResponse.body, Array<SemesterSchedule>::class.java)
            data.value = deserializedResponse
        }
    }

    private suspend fun tryRequest(request: KSuspendFunction1<String, Response>): Response? {
        var token = DeviceUser.stinfoToken

        if (token == null) {
            DeviceUser.getMybkToken()
            token = DeviceUser.stinfoToken
        }

        var scheduleResponse = token?.let { request(it) }

        if (scheduleResponse != null) {
            if (scheduleResponse.code == 302) { // Token expired
                DeviceUser.signIn()
                DeviceUser.getMybkToken()
                token = DeviceUser.stinfoToken
                scheduleResponse = token?.let { request(it) }
            }
        }

        return scheduleResponse
    }

    private suspend fun requestSchedule(token: String): Response {
        val body: RequestBody = FormBody.Builder().apply {
            add("_token", token)
        }.build()

        return Cookuest.post(
            "https://mybk.hcmut.edu.vn/stinfo/lichthi/ajax_lichhoc",
            body
        ).await()
    }
}
