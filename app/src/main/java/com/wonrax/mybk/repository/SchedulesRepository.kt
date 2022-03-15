package com.wonrax.mybk.repository

import androidx.compose.runtime.mutableStateOf
import com.google.gson.Gson
import com.wonrax.mybk.model.schedule.SemesterSchedule
import com.wonrax.mybk.network.Cookuest
import com.wonrax.mybk.network.Response
import com.wonrax.mybk.network.await
import okhttp3.FormBody
import okhttp3.RequestBody
import java.io.File

class SchedulesRepository(
    override val filesDir: File,
    override val storageFileName: String = "mybk_schedules.txt"
) : BaseRepository<SemesterSchedule> {

    override var data = mutableStateOf<Array<SemesterSchedule>?>(null)

    override fun deserialize(data: String): Array<SemesterSchedule>? {
        return Gson().fromJson(data, Array<SemesterSchedule>::class.java)
    }

    override suspend fun requestData(token: String): Response {
        val body: RequestBody = FormBody.Builder().apply {
            add("_token", token)
        }.build()

        return Cookuest().post(
            "https://mybk.hcmut.edu.vn/stinfo/lichthi/ajax_lichhoc",
            body
        ).await()
    }
}
