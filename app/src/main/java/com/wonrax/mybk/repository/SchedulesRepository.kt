package com.wonrax.mybk.repository

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import com.google.gson.Gson
import com.wonrax.mybk.model.DeviceUser
import com.wonrax.mybk.model.schedule.SemesterSchedule
import com.wonrax.mybk.network.Cookuest
import com.wonrax.mybk.network.Response
import com.wonrax.mybk.network.await
import okhttp3.FormBody
import okhttp3.RequestBody
import java.io.File
import kotlin.reflect.KSuspendFunction1

// The file name to cache data to local storage
const val FILE_NAME = "mybk_schedules.txt"

class SchedulesRepository(val context: Context) {
    var data = mutableStateOf<Array<SemesterSchedule>?>(null)

    fun getLocal(): Boolean {
        data.value = deserialize(localRead())
        return true
    }

    suspend fun getRemote() {
        val scheduleResponse = tryRequest(::requestSchedule)

        if (scheduleResponse != null) {
            val deserializedResponse = deserialize(scheduleResponse.body)
            data.value = deserializedResponse
            localStore(scheduleResponse.body)
        }
    }

    private fun deserialize(data: String): Array<SemesterSchedule>? {
        return Gson().fromJson(data, Array<SemesterSchedule>::class.java)
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

    private fun localStore(data: String) {
        val file = File(context.filesDir, FILE_NAME)
        file.writeText(data)
    }

    private fun localRead(): String {
        val file = File(context.filesDir, FILE_NAME)
        return file.readText()
    }
}
