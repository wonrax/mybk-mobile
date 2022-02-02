package com.wonrax.mybk.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.wonrax.mybk.models.DeviceUser
import com.wonrax.mybk.models.MybkState
import com.wonrax.mybk.network.Cookuest
import com.wonrax.mybk.network.await
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.RequestBody

class CourseSchedule(
    val giobd: String? = null,
    val giokt: String? = null,
    val hk_nh: String? = null,
    val ma_mh: String? = null,
    val ma_nhom: String? = null,
    val macoso: String? = null,
    val mssv: String? = null,
    val ngay_cap_nhat: String? = null,
    val nhomto: String? = null,
    val phong1: String? = null,
    val so_tin_chi: Int? = null,
    val tc_hp: Float? = null,
    val ten_hocky: String? = null,
    val ten_mh: String? = null,
    val thu1: Int? = null,
    val tiet_bd1: Int? = null,
    val tiet_kt1: Int? = null,
    val tuan_hoc: String? = null
)

class SemesterSchedule(
    val hk_nh: String? = null,
    val ngay_cap_nhat: String? = null,
    val ten_hocky: String? = null,
    val tkb: Array<CourseSchedule>? = null
)

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
