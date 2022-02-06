package com.wonrax.mybk.repository

import android.content.Context
import androidx.compose.runtime.MutableState
import com.wonrax.mybk.model.DeviceUser
import com.wonrax.mybk.network.Response
import java.io.File
import kotlin.reflect.KSuspendFunction1

interface BaseRepository<D> {
    var data: MutableState<Array<D>?>
    val context: Context

    // The file name to cache data to local storage
    val storageFileName: String

    fun getLocal(): Boolean {
        try {
            this.data.value = deserialize(localRead())
        } catch (e: Exception) {
            return false
        }
        return true
    }

    suspend fun getRemote() {
        val scheduleResponse = tryRequest(this::requestData)

        if (scheduleResponse != null) {
            val deserializedResponse = this.deserialize(scheduleResponse.body)
            this.data.value = deserializedResponse
            this.localStore(scheduleResponse.body)
        }
    }

    fun deserialize(data: String): Array<D>?

    suspend fun tryRequest(request: KSuspendFunction1<String, Response>): Response? {
        var token = DeviceUser.stinfoToken

        if (token == null) {
            DeviceUser.signIn()
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

    suspend fun requestData(token: String): Response

    fun localStore(data: String) {
        val file = File(this.context.filesDir, this.storageFileName)
        file.writeText(data)
    }

    private fun localRead(): String {
        val file = File(this.context.filesDir, this.storageFileName)
        return file.readText()
    }
}
