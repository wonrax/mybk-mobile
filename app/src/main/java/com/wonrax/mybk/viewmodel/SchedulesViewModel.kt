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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.RequestBody

class SchedulesViewModel : ViewModel() {
    val response = mutableStateOf<Array<SemesterSchedule>?>(null)

    val isLoading = mutableStateOf(true)

    val selectedSemester = mutableStateOf<SemesterSchedule?>(null)

    init {
        update()
    }

    private fun changeResponse(value: Array<SemesterSchedule>) {
        response.value = value
    }

    private fun update() {
        CoroutineScope(Dispatchers.IO).launch {
            val status = DeviceUser.getMybkToken()
            val token = DeviceUser.stinfoToken

            if (status == MybkState.LOGGED_IN) {
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

                changeResponse(deserializedResponse)
                if (deserializedResponse.isNotEmpty())
                    selectedSemester.value = deserializedResponse[0]
                isLoading.value = false
            }
        }
    }

    private fun mockUpdate() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            val scheduleResponse = """[{"tkb":[{"ma_mh":"CO4317","ten_mh":"LUAN VAN TOT NGHIEP(KHMT","nhomto":"L01","tuan_hoc":"01|02|03|04|--|--|07|08|09|--|11|12|13|14|15|16|17|18|","macoso":"BK-CS1","thu1":0,"tiet_bd1":0,"tiet_kt1":0,"phong1":"------","tc_hp":9,"so_tin_chi":9,"ma_nhom":"L01","mssv":"1812064","hk_nh":"20212","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2021 - 2022","giobd":"0:00","giokt":"0:00","ngay_cap_nhat":"2022-01-25T18:56:24.000+0000"},{"ma_mh":"CO3025","ten_mh":"PHAN TICH VA THIET KE HT","nhomto":"L01","tuan_hoc":"01|02|03|--|--|06|07|08|09|--|--|--|--|14|15|16|17|18|","macoso":"BK-CS1","thu1":6,"tiet_bd1":2,"tiet_kt1":4,"phong1":"HANGOUT_TUONGTAC","tc_hp":4,"so_tin_chi":4,"ma_nhom":"L01","mssv":"1812064","hk_nh":"20212","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2021 - 2022","giobd":"7:00","giokt":"9:50","ngay_cap_nhat":"2022-01-25T18:56:24.000+0000"},{"ma_mh":"SP1010","ten_mh":"DUONGLOI CM CUA DCSVN(BT","nhomto":"L02","tuan_hoc":"--|02|--|--|--|06|--|08|--|--|11|--|13|","macoso":"BK-CS1","thu1":6,"tiet_bd1":7,"tiet_kt1":8,"phong1":"HANGOUT_TUONGTAC","tc_hp":0,"so_tin_chi":0,"ma_nhom":"L02","mssv":"1812064","hk_nh":"20212","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2021 - 2022","giobd":"12:00","giokt":"13:50","ngay_cap_nhat":"2022-01-25T18:56:24.000+0000"},{"ma_mh":"SP1009","ten_mh":"DUONGLOI CM CUA DANGCSVN","nhomto":"L02","tuan_hoc":"01|02|03|--|--|06|07|08|09|--|--|--|--|14|15|16|17|18|","macoso":"BK-CS1","thu1":6,"tiet_bd1":9,"tiet_kt1":10,"phong1":"HANGOUT_TUONGTAC","tc_hp":3,"so_tin_chi":3,"ma_nhom":"L02","mssv":"1812064","hk_nh":"20212","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2021 - 2022","giobd":"14:00","giokt":"15:50","ngay_cap_nhat":"2022-01-25T18:56:24.000+0000"}],"ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2021 - 2022","hk_nh":"20212","ngay_cap_nhat":"2022-01-26 01:56:24.0"},{"tkb":[{"ma_mh":"CO4315","ten_mh":"\u0110\u1ec1 c\u01b0\u01a1ng lu\u1eadn v\u0103n t\u1ed1t nghi\u1ec7p (Khoa h\u1ecdc M\u00e1y t\u00ednh)                                                                        ","nhomto":"L20","tuan_hoc":"--|","macoso":"BK-CS1","thu1":0,"tiet_bd1":0,"tiet_kt1":0,"phong1":"------","tc_hp":2,"so_tin_chi":0,"ma_nhom":"L20","mssv":"1812064","hk_nh":"20211","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2021 - 2022","giobd":"0:00","giokt":"0:00","ngay_cap_nhat":"2021-12-22T03:41:38.000+0000"},{"ma_mh":"CO3029","ten_mh":"KHAI PHA DU LIEU","nhomto":"L02","tuan_hoc":"33|34|35|36|37|38|39|40|41|42|43|44|","macoso":"BK-CS1","thu1":3,"tiet_bd1":5,"tiet_kt1":6,"phong1":"HANGOUT_TUONGTAC","tc_hp":3,"so_tin_chi":3,"ma_nhom":"L02","mssv":"1812064","hk_nh":"20211","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2021 - 2022","giobd":"10:00","giokt":"11:50","ngay_cap_nhat":"2021-12-22T03:41:38.000+0000"},{"ma_mh":"SP1005","ten_mh":"T\u01b0 t\u01b0\u1edfng H\u1ed3 Ch\u00ed Minh                                                                                                    ","nhomto":"L05","tuan_hoc":"33|34|--|36|37|38|39|40|41|42|43|","macoso":"BK-CS1","thu1":5,"tiet_bd1":2,"tiet_kt1":3,"phong1":"HANGOUT_TUONGTAC","tc_hp":2,"so_tin_chi":2,"ma_nhom":"L05","mssv":"1812064","hk_nh":"20211","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2021 - 2022","giobd":"7:00","giokt":"8:50","ngay_cap_nhat":"2021-12-22T03:41:38.000+0000"},{"ma_mh":"CO3015","ten_mh":"KIEM TRA PHAN MEM","nhomto":"L01","tuan_hoc":"33|34|--|36|37|38|39|40|41|42|43|44|","macoso":"BK-CS1","thu1":6,"tiet_bd1":2,"tiet_kt1":3,"phong1":"HANGOUT_TUONGTAC","tc_hp":3,"so_tin_chi":3,"ma_nhom":"L01","mssv":"1812064","hk_nh":"20211","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2021 - 2022","giobd":"7:00","giokt":"8:50","ngay_cap_nhat":"2021-12-22T03:41:38.000+0000"},{"ma_mh":"CO3085","ten_mh":"XU LY NGON NGU TU NHIEN","nhomto":"L01","tuan_hoc":"33|34|--|36|37|38|39|40|41|42|43|44|45|46|47|","macoso":"BK-CS1","thu1":6,"tiet_bd1":4,"tiet_kt1":5,"phong1":"HANGOUT_TUONGTAC","tc_hp":3,"so_tin_chi":3,"ma_nhom":"L01","mssv":"1812064","hk_nh":"20211","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2021 - 2022","giobd":"9:00","giokt":"10:50","ngay_cap_nhat":"2021-12-22T03:41:38.000+0000"},{"ma_mh":"CO3061","ten_mh":"NHAP MON TRITUE NHAN TAO","nhomto":"L02","tuan_hoc":"33|34|--|36|37|38|39|40|41|42|43|44|45|","macoso":"BK-CS1","thu1":6,"tiet_bd1":8,"tiet_kt1":9,"phong1":"HANGOUT_TUONGTAC","tc_hp":3,"so_tin_chi":3,"ma_nhom":"L02","mssv":"1812064","hk_nh":"20211","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2021 - 2022","giobd":"13:00","giokt":"14:50","ngay_cap_nhat":"2021-12-22T03:41:38.000+0000"}],"ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2021 - 2022","hk_nh":"20211","ngay_cap_nhat":"2021-12-22 10:41:38.0"},{"tkb":[{"ma_mh":"CO3315","ten_mh":"Th\u1ef1c t\u1eadp ngo\u00e0i tr\u01b0\u1eddng                                                                                                   ","nhomto":"L01","tuan_hoc":"--|23|24|25|26|27|28|29|","macoso":"BK-CS1","thu1":0,"tiet_bd1":0,"tiet_kt1":0,"phong1":"------","tc_hp":2,"so_tin_chi":2,"ma_nhom":"L01","mssv":"1812064","hk_nh":"20203","ten_hocky":"H\u1ecdc k\u1ef3 3 N\u0103m h\u1ecdc 2020 - 2021","giobd":"0:00","giokt":"0:00","ngay_cap_nhat":"2021-10-22T08:51:09.000+0000"}],"ten_hocky":"H\u1ecdc k\u1ef3 3 N\u0103m h\u1ecdc 2020 - 2021","hk_nh":"20203","ngay_cap_nhat":"2021-10-22 15:51:09.0"},{"tkb":[{"ma_mh":"CO3087","ten_mh":"Th\u1ef1c t\u1eadp \u0111\u1ed3 \u00e1n \u0111a ng\u00e0nh                                                                                                 ","nhomto":"L14","tuan_hoc":"--|","macoso":"BK-CS1","thu1":0,"tiet_bd1":0,"tiet_kt1":0,"phong1":"------","tc_hp":2,"so_tin_chi":2,"ma_nhom":"L14","mssv":"1812064","hk_nh":"20202","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2020 - 2021","giobd":"0:00","giokt":"0:00","ngay_cap_nhat":"2021-07-07T05:38:43.000+0000"},{"ma_mh":"CO3031","ten_mh":"Ph\u00e2n t\u00edch v\u00e0 thi\u1ebft k\u1ebf gi\u1ea3i thu\u1eadt                                                                                        ","nhomto":"L02","tuan_hoc":"--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|23|","macoso":"BK-CS2","thu1":2,"tiet_bd1":2,"tiet_kt1":4,"phong1":"H2-403","tc_hp":3,"so_tin_chi":3,"ma_nhom":"L02","mssv":"1812064","hk_nh":"20202","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2020 - 2021","giobd":"7:00","giokt":"9:50","ngay_cap_nhat":"2021-07-07T05:38:43.000+0000"},{"ma_mh":"CO3067","ten_mh":"TINH TOAN SONG SONG","nhomto":"L02","tuan_hoc":"--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|23|","macoso":"BK-CS2","thu1":2,"tiet_bd1":5,"tiet_kt1":6,"phong1":"H6-110","tc_hp":3,"so_tin_chi":3,"ma_nhom":"L02","mssv":"1812064","hk_nh":"20202","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2020 - 2021","giobd":"10:00","giokt":"11:50","ngay_cap_nhat":"2021-07-07T05:38:43.000+0000"},{"ma_mh":"CO3068","ten_mh":"TINH TOAN SONG SONG (TN)","nhomto":"L04","tuan_hoc":"--|","macoso":"BK-CS2","thu1":2,"tiet_bd1":10,"tiet_kt1":12,"phong1":"H6-702","tc_hp":0,"so_tin_chi":0,"ma_nhom":"L04","mssv":"1812064","hk_nh":"20202","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2020 - 2021","giobd":"15:00","giokt":"17:50","ngay_cap_nhat":"2021-07-07T05:38:43.000+0000"},{"ma_mh":"CO3068","ten_mh":"TINH TOAN SONG SONG (TN)","nhomto":"L04","tuan_hoc":"--|","macoso":"BK-CS1","thu1":2,"tiet_bd1":10,"tiet_kt1":12,"phong1":"HANGOUT_TUONGTAC","tc_hp":0,"so_tin_chi":0,"ma_nhom":"L04","mssv":"1812064","hk_nh":"20202","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2020 - 2021","giobd":"15:00","giokt":"17:50","ngay_cap_nhat":"2021-07-07T05:38:43.000+0000"},{"ma_mh":"CO3069","ten_mh":"MAT MA VA AN NINH MANG","nhomto":"L03","tuan_hoc":"--|09|10|11|12|13|14|15|16|17|18|19|20|","macoso":"BK-CS2","thu1":3,"tiet_bd1":3,"tiet_kt1":4,"phong1":"H2-402","tc_hp":3,"so_tin_chi":3,"ma_nhom":"L03","mssv":"1812064","hk_nh":"20202","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2020 - 2021","giobd":"8:00","giokt":"9:50","ngay_cap_nhat":"2021-07-07T05:38:43.000+0000"},{"ma_mh":"SP1003","ten_mh":"Nh\u1eefng nguy\u00ean l\u00fd c\u01a1 b\u1ea3n c\u1ee7a ch\u1ee7 ngh\u0129a M\u00e1c-L\u00eanin                                                                          ","nhomto":"L01","tuan_hoc":"--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|23|","macoso":"BK-CS2","thu1":3,"tiet_bd1":4,"tiet_kt1":5,"phong1":"H2-307","tc_hp":5,"so_tin_chi":5,"ma_nhom":"L01","mssv":"1812064","hk_nh":"20202","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2020 - 2021","giobd":"9:00","giokt":"10:50","ngay_cap_nhat":"2021-07-07T05:38:43.000+0000"},{"ma_mh":"CO3031","ten_mh":"Ph\u00e2n t\u00edch v\u00e0 thi\u1ebft k\u1ebf gi\u1ea3i thu\u1eadt                                                                                        ","nhomto":"L02","tuan_hoc":"--|09|10|11|12|13|14|15|--|17|18|19|20|","macoso":"BK-CS2","thu1":4,"tiet_bd1":2,"tiet_kt1":4,"phong1":"H2-403","tc_hp":3,"so_tin_chi":3,"ma_nhom":"L02","mssv":"1812064","hk_nh":"20202","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2020 - 2021","giobd":"7:00","giokt":"9:50","ngay_cap_nhat":"2021-07-07T05:38:43.000+0000"},{"ma_mh":"CO3067","ten_mh":"TINH TOAN SONG SONG","nhomto":"L02","tuan_hoc":"--|09|10|11|12|13|14|15|--|17|18|19|20|","macoso":"BK-CS2","thu1":4,"tiet_bd1":5,"tiet_kt1":6,"phong1":"H6-110","tc_hp":3,"so_tin_chi":3,"ma_nhom":"L02","mssv":"1812064","hk_nh":"20202","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2020 - 2021","giobd":"10:00","giokt":"11:50","ngay_cap_nhat":"2021-07-07T05:38:43.000+0000"},{"ma_mh":"CO3068","ten_mh":"TINH TOAN SONG SONG (TN)","nhomto":"L04","tuan_hoc":"--|--|10|11|12|13|14|--|--|17|18|","macoso":"BK-CS2","thu1":4,"tiet_bd1":10,"tiet_kt1":12,"phong1":"H6-702","tc_hp":0,"so_tin_chi":0,"ma_nhom":"L04","mssv":"1812064","hk_nh":"20202","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2020 - 2021","giobd":"15:00","giokt":"17:50","ngay_cap_nhat":"2021-07-07T05:38:43.000+0000"},{"ma_mh":"CO3068","ten_mh":"TINH TOAN SONG SONG (TN)","nhomto":"L04","tuan_hoc":"--|","macoso":"BK-CS1","thu1":4,"tiet_bd1":10,"tiet_kt1":12,"phong1":"HANGOUT_TUONGTAC","tc_hp":0,"so_tin_chi":0,"ma_nhom":"L04","mssv":"1812064","hk_nh":"20202","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2020 - 2021","giobd":"15:00","giokt":"17:50","ngay_cap_nhat":"2021-07-07T05:38:43.000+0000"},{"ma_mh":"SP1003","ten_mh":"Nh\u1eefng nguy\u00ean l\u00fd c\u01a1 b\u1ea3n c\u1ee7a ch\u1ee7 ngh\u0129a M\u00e1c-L\u00eanin                                                                          ","nhomto":"L01","tuan_hoc":"--|09|10|11|12|13|14|15|16|17|18|19|20|","macoso":"BK-CS2","thu1":5,"tiet_bd1":4,"tiet_kt1":6,"phong1":"H6-308","tc_hp":5,"so_tin_chi":5,"ma_nhom":"L01","mssv":"1812064","hk_nh":"20202","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2020 - 2021","giobd":"9:00","giokt":"11:50","ngay_cap_nhat":"2021-07-07T05:38:43.000+0000"},{"ma_mh":"CO3070","ten_mh":"MAT MA &AN NINH MANG(TN)","nhomto":"L05","tuan_hoc":"--|--|--|11|12|13|14|--|16|17|18|","macoso":"BK-CS2","thu1":5,"tiet_bd1":8,"tiet_kt1":10,"phong1":"H6-603","tc_hp":0,"so_tin_chi":0,"ma_nhom":"L05","mssv":"1812064","hk_nh":"20202","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2020 - 2021","giobd":"13:00","giokt":"15:50","ngay_cap_nhat":"2021-07-07T05:38:43.000+0000"},{"ma_mh":"CO3070","ten_mh":"MAT MA &AN NINH MANG(TN)","nhomto":"L05","tuan_hoc":"--|","macoso":"BK-CS1","thu1":5,"tiet_bd1":8,"tiet_kt1":10,"phong1":"HANGOUT_TUONGTAC","tc_hp":0,"so_tin_chi":0,"ma_nhom":"L05","mssv":"1812064","hk_nh":"20202","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2020 - 2021","giobd":"13:00","giokt":"15:50","ngay_cap_nhat":"2021-07-07T05:38:43.000+0000"},{"ma_mh":"SP1003","ten_mh":"Nh\u1eefng nguy\u00ean l\u00fd c\u01a1 b\u1ea3n c\u1ee7a ch\u1ee7 ngh\u0129a M\u00e1c-L\u00eanin                                                                          ","nhomto":"L01","tuan_hoc":"--|09|10|11|12|13|14|15|16|--|18|19|20|","macoso":"BK-CS2","thu1":6,"tiet_bd1":4,"tiet_kt1":5,"phong1":"H2-307","tc_hp":5,"so_tin_chi":5,"ma_nhom":"L01","mssv":"1812064","hk_nh":"20202","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2020 - 2021","giobd":"9:00","giokt":"10:50","ngay_cap_nhat":"2021-07-07T05:38:43.000+0000"}],"ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2020 - 2021","hk_nh":"20202","ngay_cap_nhat":"2021-07-07 12:38:43.0"},{"tkb":[{"ma_mh":"CO3003","ten_mh":"M\u1ea1ng m\u00e1y t\u00ednh                                                                                                           ","nhomto":"L01","tuan_hoc":"39|40|41|42|43|44|--|46|47|48|49|50|51|","macoso":"BK-CS2","thu1":3,"tiet_bd1":3,"tiet_kt1":5,"phong1":"H6-311","tc_hp":4,"so_tin_chi":4,"ma_nhom":"L01","mssv":"1812064","hk_nh":"20201","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2020 - 2021","giobd":"8:00","giokt":"10:50","ngay_cap_nhat":"2021-01-14T06:44:46.000+0000"},{"ma_mh":"CO3004","ten_mh":"M\u1ea1ng m\u00e1y t\u00ednh (tn)                                                                                                      ","nhomto":"L04","tuan_hoc":"--|--|41|42|43|44|--|46|47|48|49|50|--|52|53|","macoso":"BK-CS2","thu1":5,"tiet_bd1":7,"tiet_kt1":8,"phong1":"H6-703","tc_hp":0,"so_tin_chi":0,"ma_nhom":"L04","mssv":"1812064","hk_nh":"20201","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2020 - 2021","giobd":"12:00","giokt":"13:50","ngay_cap_nhat":"2021-01-14T06:44:46.000+0000"}],"ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2020 - 2021","hk_nh":"20201","ngay_cap_nhat":"2021-01-14 13:44:46.0"},{"tkb":[{"ma_mh":"AS2001","ten_mh":"CO HOC UNG DUNG","nhomto":"DT01","tuan_hoc":"--|32|33|34|35|36|37|","macoso":"BK-CS1","thu1":2,"tiet_bd1":7,"tiet_kt1":9,"phong1":"C5-502","tc_hp":3,"so_tin_chi":3,"ma_nhom":"DT01","mssv":"1812064","hk_nh":"20193","ten_hocky":"H\u1ecdc k\u1ef3 3 N\u0103m h\u1ecdc 2019 - 2020","giobd":"12:00","giokt":"14:50","ngay_cap_nhat":"2020-09-08T09:04:13.000+0000"},{"ma_mh":"AS2001","ten_mh":"CO HOC UNG DUNG","nhomto":"DT01","tuan_hoc":"--|--|--|--|--|36|","macoso":"BK-CS1","thu1":2,"tiet_bd1":58,"tiet_kt1":58,"phong1":"HANGOUT_TUONGTAC","tc_hp":3,"so_tin_chi":3,"ma_nhom":"DT01","mssv":"1812064","hk_nh":"20193","ten_hocky":"H\u1ecdc k\u1ef3 3 N\u0103m h\u1ecdc 2019 - 2020","giobd":"18:15","giokt":"19:30","ngay_cap_nhat":"2020-09-08T09:04:13.000+0000"},{"ma_mh":"AS2001","ten_mh":"CO HOC UNG DUNG","nhomto":"DT01","tuan_hoc":"--|32|33|34|35|--|37|","macoso":"BK-CS1","thu1":2,"tiet_bd1":58,"tiet_kt1":58,"phong1":"HANGOUT_TUONGTAC","tc_hp":3,"so_tin_chi":3,"ma_nhom":"DT01","mssv":"1812064","hk_nh":"20193","ten_hocky":"H\u1ecdc k\u1ef3 3 N\u0103m h\u1ecdc 2019 - 2020","giobd":"18:15","giokt":"19:30","ngay_cap_nhat":"2020-09-08T09:04:13.000+0000"},{"ma_mh":"AS2001","ten_mh":"CO HOC UNG DUNG","nhomto":"DT01","tuan_hoc":"--|32|33|34|35|--|37|","macoso":"BK-CS1","thu1":4,"tiet_bd1":7,"tiet_kt1":9,"phong1":"C5-502","tc_hp":3,"so_tin_chi":3,"ma_nhom":"DT01","mssv":"1812064","hk_nh":"20193","ten_hocky":"H\u1ecdc k\u1ef3 3 N\u0103m h\u1ecdc 2019 - 2020","giobd":"12:00","giokt":"14:50","ngay_cap_nhat":"2020-09-08T09:04:13.000+0000"},{"ma_mh":"AS2001","ten_mh":"CO HOC UNG DUNG","nhomto":"DT01","tuan_hoc":"--|32|33|34|35|--|37|","macoso":"BK-CS1","thu1":4,"tiet_bd1":58,"tiet_kt1":58,"phong1":"HANGOUT_TUONGTAC","tc_hp":3,"so_tin_chi":3,"ma_nhom":"DT01","mssv":"1812064","hk_nh":"20193","ten_hocky":"H\u1ecdc k\u1ef3 3 N\u0103m h\u1ecdc 2019 - 2020","giobd":"18:15","giokt":"19:30","ngay_cap_nhat":"2020-09-08T09:04:13.000+0000"}],"ten_hocky":"H\u1ecdc k\u1ef3 3 N\u0103m h\u1ecdc 2019 - 2020","hk_nh":"20193","ngay_cap_nhat":"2020-09-08 16:04:13.0"},{"tkb":[{"ma_mh":"CO3055","ten_mh":"Th\u1ef1c t\u1eadp c\u00f4ng ngh\u1ec7 ph\u1ea7n m\u1ec1m                                                                                             ","nhomto":"L02","tuan_hoc":"--|","macoso":"BK-CS1","thu1":0,"tiet_bd1":0,"tiet_kt1":0,"phong1":"------","tc_hp":2,"so_tin_chi":2,"ma_nhom":"L02","mssv":"1812064","hk_nh":"20192","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2019 - 2020","giobd":"0:00","giokt":"0:00","ngay_cap_nhat":"2020-06-22T02:42:27.000+0000"},{"ma_mh":"CO3001","ten_mh":"CONG NGHE PHAN MEM","nhomto":"L02","tuan_hoc":"--|--|--|--|--|--|--|--|--|15|16|17|18|19|20|21|--|23|24|25|","macoso":"BK-CS2","thu1":2,"tiet_bd1":7,"tiet_kt1":9,"phong1":"H2-403","tc_hp":3,"so_tin_chi":3,"ma_nhom":"L02","mssv":"1812064","hk_nh":"20192","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2019 - 2020","giobd":"12:00","giokt":"14:50","ngay_cap_nhat":"2020-06-22T02:42:27.000+0000"},{"ma_mh":"CO2001","ten_mh":"KYNANG CHNGHIEP CHO KYSU","nhomto":"L01","tuan_hoc":"--|--|--|--|--|--|--|--|--|15|16|17|18|19|20|21|--|23|24|25|26|27|","macoso":"BK-CS2","thu1":2,"tiet_bd1":10,"tiet_kt1":12,"phong1":"H6-114","tc_hp":3,"so_tin_chi":3,"ma_nhom":"L01","mssv":"1812064","hk_nh":"20192","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2019 - 2020","giobd":"15:00","giokt":"17:50","ngay_cap_nhat":"2020-06-22T02:42:27.000+0000"},{"ma_mh":"CO2017","ten_mh":"HE DIEU HANH","nhomto":"L01","tuan_hoc":"--|--|--|--|--|--|--|--|--|15|16|17|18|19|20|21|--|23|","macoso":"BK-CS2","thu1":3,"tiet_bd1":2,"tiet_kt1":4,"phong1":"H6-514","tc_hp":3,"so_tin_chi":3,"ma_nhom":"L01","mssv":"1812064","hk_nh":"20192","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2019 - 2020","giobd":"7:00","giokt":"9:50","ngay_cap_nhat":"2020-06-22T02:42:27.000+0000"},{"ma_mh":"CO2011","ten_mh":"M\u00f4 h\u00ecnh h\u00f3a to\u00e1n h\u1ecdc                                                                                                    ","nhomto":"L01","tuan_hoc":"--|--|--|--|--|--|--|--|--|15|16|17|18|19|20|21|--|23|24|25|26|27|","macoso":"BK-CS2","thu1":3,"tiet_bd1":7,"tiet_kt1":9,"phong1":"H2-404","tc_hp":3,"so_tin_chi":3,"ma_nhom":"L01","mssv":"1812064","hk_nh":"20192","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2019 - 2020","giobd":"12:00","giokt":"14:50","ngay_cap_nhat":"2020-06-22T02:42:27.000+0000"},{"ma_mh":"CO2018","ten_mh":"HE DIEU HANH (TN)","nhomto":"L04","tuan_hoc":"--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|22|23|24|25|26|","macoso":"BK-CS2","thu1":4,"tiet_bd1":8,"tiet_kt1":9,"phong1":"H6-703","tc_hp":0,"so_tin_chi":0,"ma_nhom":"L04","mssv":"1812064","hk_nh":"20192","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2019 - 2020","giobd":"13:00","giokt":"14:50","ngay_cap_nhat":"2020-06-22T02:42:27.000+0000"},{"ma_mh":"MT2001","ten_mh":"X\u00e1c su\u1ea5t v\u00e0 th\u1ed1ng k\u00ea                                                                                                    ","nhomto":"L04","tuan_hoc":"--|--|--|--|--|--|--|--|--|15|16|17|18|19|20|21|--|23|24|25|26|","macoso":"BK-CS2","thu1":6,"tiet_bd1":2,"tiet_kt1":4,"phong1":"H2-201","tc_hp":3,"so_tin_chi":3,"ma_nhom":"L04","mssv":"1812064","hk_nh":"20192","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2019 - 2020","giobd":"7:00","giokt":"9:50","ngay_cap_nhat":"2020-06-22T02:42:27.000+0000"},{"ma_mh":"CO2018","ten_mh":"HE DIEU HANH (TN)","nhomto":"L04","tuan_hoc":"--|","macoso":"BK-CS2","thu1":8,"tiet_bd1":5,"tiet_kt1":6,"phong1":"H6-702","tc_hp":0,"so_tin_chi":0,"ma_nhom":"L04","mssv":"1812064","hk_nh":"20192","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2019 - 2020","giobd":"10:00","giokt":"11:50","ngay_cap_nhat":"2020-06-22T02:42:27.000+0000"}],"ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2019 - 2020","hk_nh":"20192","ngay_cap_nhat":"2020-06-22 09:42:27.0"},{"tkb":[{"ma_mh":"CO2007","ten_mh":"KIEN TRUC MAY TINH","nhomto":"L01","tuan_hoc":"--|35|--|37|38|39|40|41|--|--|44|45|46|47|48|49|50|51|","macoso":"BK-CS2","thu1":2,"tiet_bd1":2,"tiet_kt1":4,"phong1":"H6-206","tc_hp":4,"so_tin_chi":4,"ma_nhom":"L01","mssv":"1812064","hk_nh":"20191","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2019 - 2020","giobd":"7:00","giokt":"9:50","ngay_cap_nhat":"2019-11-25T03:43:38.000+0000"},{"ma_mh":"CO2003","ten_mh":"CTRUC DULIEU & GIAITHUAT","nhomto":"L03","tuan_hoc":"--|35|--|37|38|39|40|41|--|--|44|45|46|47|48|49|50|51|","macoso":"BK-CS2","thu1":2,"tiet_bd1":7,"tiet_kt1":9,"phong1":"H6-213","tc_hp":4,"so_tin_chi":4,"ma_nhom":"L03","mssv":"1812064","hk_nh":"20191","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2019 - 2020","giobd":"12:00","giokt":"14:50","ngay_cap_nhat":"2019-11-25T03:43:38.000+0000"},{"ma_mh":"CO2005","ten_mh":"L\u1eadp tr\u00ecnh h\u01b0\u1edbng \u0111\u1ed1i t\u01b0\u1ee3ng                                                                                               ","nhomto":"L04","tuan_hoc":"--|35|36|37|38|39|40|41|--|--|44|45|46|47|48|49|50|51|","macoso":"BK-CS2","thu1":3,"tiet_bd1":7,"tiet_kt1":9,"phong1":"H6-413","tc_hp":4,"so_tin_chi":4,"ma_nhom":"L04","mssv":"1812064","hk_nh":"20191","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2019 - 2020","giobd":"12:00","giokt":"14:50","ngay_cap_nhat":"2019-11-25T03:43:38.000+0000"},{"ma_mh":"CO2007","ten_mh":"KIEN TRUC MAY TINH","nhomto":"L01","tuan_hoc":"--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|51|","macoso":"BK-CS2","thu1":4,"tiet_bd1":2,"tiet_kt1":4,"phong1":"H6-206","tc_hp":4,"so_tin_chi":4,"ma_nhom":"L01","mssv":"1812064","hk_nh":"20191","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2019 - 2020","giobd":"7:00","giokt":"9:50","ngay_cap_nhat":"2019-11-25T03:43:38.000+0000"},{"ma_mh":"CO2008","ten_mh":"KIEN TRUC MAY TINH (TH)","nhomto":"L02","tuan_hoc":"--|--|--|--|38|39|40|--|--|43|44|45|46|47|48|49|","macoso":"BK-CS2","thu1":4,"tiet_bd1":2,"tiet_kt1":4,"phong1":"H6-703","tc_hp":0,"so_tin_chi":0,"ma_nhom":"L02","mssv":"1812064","hk_nh":"20191","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2019 - 2020","giobd":"7:00","giokt":"9:50","ngay_cap_nhat":"2019-11-25T03:43:38.000+0000"},{"ma_mh":"CO2003","ten_mh":"CTRUC DULIEU & GIAITHUAT","nhomto":"L03","tuan_hoc":"--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|51|","macoso":"BK-CS2","thu1":4,"tiet_bd1":7,"tiet_kt1":9,"phong1":"H6-213","tc_hp":4,"so_tin_chi":4,"ma_nhom":"L03","mssv":"1812064","hk_nh":"20191","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2019 - 2020","giobd":"12:00","giokt":"14:50","ngay_cap_nhat":"2019-11-25T03:43:38.000+0000"},{"ma_mh":"CO2004","ten_mh":"CTRUC DULIEU &G\/THUAT(TH","nhomto":"L06","tuan_hoc":"--|--|36|37|38|39|40|--|--|43|44|--|--|47|--|49|50|","macoso":"BK-CS2","thu1":4,"tiet_bd1":7,"tiet_kt1":9,"phong1":"H6-702","tc_hp":0,"so_tin_chi":0,"ma_nhom":"L06","mssv":"1812064","hk_nh":"20191","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2019 - 2020","giobd":"12:00","giokt":"14:50","ngay_cap_nhat":"2019-11-25T03:43:38.000+0000"},{"ma_mh":"CO2006","ten_mh":"L\u1eadp tr\u00ecnh h\u01b0\u1edbng \u0111\u1ed1i t\u01b0\u1ee3ng (th\u1ef1c h\u00e0nh)                                                                                   ","nhomto":"L08","tuan_hoc":"34|35|36|37|38|39|40|--|--|43|44|45|46|47|48|49|50|","macoso":"BK-CS2","thu1":4,"tiet_bd1":10,"tiet_kt1":11,"phong1":"H6-701","tc_hp":0,"so_tin_chi":0,"ma_nhom":"L08","mssv":"1812064","hk_nh":"20191","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2019 - 2020","giobd":"15:00","giokt":"16:50","ngay_cap_nhat":"2019-11-25T03:43:38.000+0000"},{"ma_mh":"MT1009","ten_mh":"PHUONG PHAP TINH","nhomto":"L20","tuan_hoc":"34|35|36|37|38|39|40|--|--|43|44|45|46|47|48|49|50|","macoso":"BK-CS2","thu1":5,"tiet_bd1":7,"tiet_kt1":9,"phong1":"H2-301","tc_hp":3,"so_tin_chi":3,"ma_nhom":"L20","mssv":"1812064","hk_nh":"20191","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2019 - 2020","giobd":"12:00","giokt":"14:50","ngay_cap_nhat":"2019-11-25T03:43:38.000+0000"},{"ma_mh":"SP1007","ten_mh":"PH\/LUAT VIETNAM DAICUONG","nhomto":"L22","tuan_hoc":"34|35|36|37|38|39|40|--|--|43|44|45|46|47|48|49|50|","macoso":"BK-CS2","thu1":6,"tiet_bd1":4,"tiet_kt1":5,"phong1":"H6-211","tc_hp":2,"so_tin_chi":2,"ma_nhom":"L22","mssv":"1812064","hk_nh":"20191","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2019 - 2020","giobd":"9:00","giokt":"10:50","ngay_cap_nhat":"2019-11-25T03:43:38.000+0000"},{"ma_mh":"PE1011","ten_mh":"B\u00f3ng chuy\u1ec1n (h\u1ecdc ph\u1ea7n 1)                                                                                                ","nhomto":"L20","tuan_hoc":"34|35|36|37|38|39|40|--|--|43|44|45|46|47|48|49|50|","macoso":"BK-CS2","thu1":6,"tiet_bd1":7,"tiet_kt1":8,"phong1":"CS2-NHATHIDAU-SAN1","tc_hp":1.5,"so_tin_chi":0,"ma_nhom":"L20","mssv":"1812064","hk_nh":"20191","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2019 - 2020","giobd":"12:00","giokt":"13:50","ngay_cap_nhat":"2019-11-25T03:43:38.000+0000"}],"ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2019 - 2020","hk_nh":"20191","ngay_cap_nhat":"2019-11-25 10:43:38.0"},{"tkb":[{"ma_mh":"CO1009","ten_mh":"H\u1ec7 th\u1ed1ng s\u1ed1                                                                                                             ","nhomto":"L03","tuan_hoc":"03|04|--|--|07|08|09|10|11|--|13|14|15|--|17|--|19|20|21|","macoso":"BK-CS2","thu1":2,"tiet_bd1":7,"tiet_kt1":10,"phong1":"H1-301","tc_hp":4,"so_tin_chi":4,"ma_nhom":"L03","mssv":"1812064","hk_nh":"20182","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2018 - 2019","giobd":"12:00","giokt":"15:50","ngay_cap_nhat":"2019-06-27T09:43:13.000+0000"},{"ma_mh":"MT1006","ten_mh":"GIAI TICH 2 (BT)","nhomto":"L36","tuan_hoc":"03|04|--|--|07|08|09|10|11|--|13|14|15|16|17|--|19|20|21|","macoso":"BK-CS2","thu1":3,"tiet_bd1":2,"tiet_kt1":3,"phong1":"H1-401","tc_hp":0,"so_tin_chi":0,"ma_nhom":"L36","mssv":"1812064","hk_nh":"20182","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2018 - 2019","giobd":"7:00","giokt":"8:50","ngay_cap_nhat":"2019-06-27T09:43:13.000+0000"},{"ma_mh":"PH1007","ten_mh":"THI NGHIEM VAT LY","nhomto":"L38","tuan_hoc":"03|04|--|--|07|08|09|10|11|--|13|14|15|16|17|--|19|20|21|","macoso":"BK-CS2","thu1":3,"tiet_bd1":7,"tiet_kt1":8,"phong1":"H1-602","tc_hp":1,"so_tin_chi":1,"ma_nhom":"L38","mssv":"1812064","hk_nh":"20182","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2018 - 2019","giobd":"12:00","giokt":"13:50","ngay_cap_nhat":"2019-06-27T09:43:13.000+0000"},{"ma_mh":"MT1008","ten_mh":"DAI SO TUYEN TINH (BT)","nhomto":"L14","tuan_hoc":"03|04|--|--|07|08|09|10|11|--|13|14|15|16|17|--|19|20|21|","macoso":"BK-CS2","thu1":3,"tiet_bd1":9,"tiet_kt1":10,"phong1":"H1-304","tc_hp":0,"so_tin_chi":0,"ma_nhom":"L14","mssv":"1812064","hk_nh":"20182","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2018 - 2019","giobd":"14:00","giokt":"15:50","ngay_cap_nhat":"2019-06-27T09:43:13.000+0000"},{"ma_mh":"MT1007","ten_mh":"DAI SO TUYEN TINH","nhomto":"L10","tuan_hoc":"03|04|--|--|07|08|09|10|11|--|13|14|15|16|17|--|19|20|21|","macoso":"BK-CS2","thu1":4,"tiet_bd1":2,"tiet_kt1":3,"phong1":"H2-406","tc_hp":3,"so_tin_chi":3,"ma_nhom":"L10","mssv":"1812064","hk_nh":"20182","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2018 - 2019","giobd":"7:00","giokt":"8:50","ngay_cap_nhat":"2019-06-27T09:43:13.000+0000"},{"ma_mh":"PE1015","ten_mh":"B\u00f3ng r\u1ed5 (h\u1ecdc ph\u1ea7n 1)                                                                                                    ","nhomto":"L13","tuan_hoc":"03|04|--|--|07|08|09|10|11|--|13|14|15|16|17|--|19|20|21|","macoso":"BK-CS2","thu1":4,"tiet_bd1":4,"tiet_kt1":5,"phong1":"CS2-NHATHIDAU-SAN1","tc_hp":1.5,"so_tin_chi":0,"ma_nhom":"L13","mssv":"1812064","hk_nh":"20182","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2018 - 2019","giobd":"9:00","giokt":"10:50","ngay_cap_nhat":"2019-06-27T09:43:13.000+0000"},{"ma_mh":"MT1005","ten_mh":"GIAI TICH 2","nhomto":"L22","tuan_hoc":"03|04|--|--|07|08|09|10|11|--|13|14|15|16|17|18|19|20|","macoso":"BK-CS2","thu1":5,"tiet_bd1":2,"tiet_kt1":4,"phong1":"H2-406","tc_hp":4,"so_tin_chi":4,"ma_nhom":"L22","mssv":"1812064","hk_nh":"20182","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2018 - 2019","giobd":"7:00","giokt":"9:50","ngay_cap_nhat":"2019-06-27T09:43:13.000+0000"},{"ma_mh":"CO1009","ten_mh":"H\u1ec7 th\u1ed1ng s\u1ed1                                                                                                             ","nhomto":"L03","tuan_hoc":"--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|21|","macoso":"BK-CS2","thu1":5,"tiet_bd1":7,"tiet_kt1":10,"phong1":"H1-301","tc_hp":4,"so_tin_chi":4,"ma_nhom":"L03","mssv":"1812064","hk_nh":"20182","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2018 - 2019","giobd":"12:00","giokt":"15:50","ngay_cap_nhat":"2019-06-27T09:43:13.000+0000"},{"ma_mh":"CO1011","ten_mh":"K\u1ef9 thu\u1eadt l\u1eadp tr\u00ecnh                                                                                                      ","nhomto":"L04","tuan_hoc":"03|04|--|--|07|08|09|10|11|--|13|14|15|16|17|18|19|20|","macoso":"BK-CS2","thu1":6,"tiet_bd1":2,"tiet_kt1":4,"phong1":"H2-206","tc_hp":4,"so_tin_chi":4,"ma_nhom":"L04","mssv":"1812064","hk_nh":"20182","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2018 - 2019","giobd":"7:00","giokt":"9:50","ngay_cap_nhat":"2019-06-27T09:43:13.000+0000"},{"ma_mh":"CO1010","ten_mh":"H\u1ec7 th\u1ed1ng s\u1ed1 (th\u00ed nghi\u1ec7m)                                                                                                ","nhomto":"L05","tuan_hoc":"--|--|--|--|--|--|--|10|--|--|--|14|--|16|--|18|--|20|","macoso":"BK-CS2","thu1":6,"tiet_bd1":7,"tiet_kt1":9,"phong1":"H6-601","tc_hp":0,"so_tin_chi":0,"ma_nhom":"L05","mssv":"1812064","hk_nh":"20182","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2018 - 2019","giobd":"12:00","giokt":"14:50","ngay_cap_nhat":"2019-06-27T09:43:13.000+0000"},{"ma_mh":"CO1012","ten_mh":"K\u1ef9 thu\u1eadt l\u1eadp tr\u00ecnh (b\u00e0i t\u1eadp)                                                                                            ","nhomto":"L05","tuan_hoc":"--|--|--|--|--|--|--|10|11|--|13|14|15|16|17|18|19|20|","macoso":"BK-CS2","thu1":6,"tiet_bd1":10,"tiet_kt1":12,"phong1":"H6-605","tc_hp":0,"so_tin_chi":0,"ma_nhom":"L05","mssv":"1812064","hk_nh":"20182","ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2018 - 2019","giobd":"15:00","giokt":"17:50","ngay_cap_nhat":"2019-06-27T09:43:13.000+0000"}],"ten_hocky":"H\u1ecdc k\u1ef3 2 N\u0103m h\u1ecdc 2018 - 2019","hk_nh":"20182","ngay_cap_nhat":"2019-06-27 16:43:13.0"},{"tkb":[{"ma_mh":"MI1003","ten_mh":"GIAO DUC QUOC PHONG","nhomto":"L64","tuan_hoc":"--|--|--|--|--|--|--|41|42|43|44|","macoso":"BK-CS1","thu1":0,"tiet_bd1":0,"tiet_kt1":0,"phong1":"------","tc_hp":1,"so_tin_chi":0,"ma_nhom":"L64","mssv":"1812064","hk_nh":"20181","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2018 - 2019","giobd":"0:00","giokt":"0:00","ngay_cap_nhat":"2018-10-01T08:10:32.000+0000"},{"ma_mh":"CH1003","ten_mh":"HOA DAI CUONG","nhomto":"L14","tuan_hoc":"34|35|36|37|38|39|40|--|--|--|--|45|46|47|","macoso":"BK-CS2","thu1":2,"tiet_bd1":1,"tiet_kt1":4,"phong1":"H1-213","tc_hp":3,"so_tin_chi":3,"ma_nhom":"L14","mssv":"1812064","hk_nh":"20181","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2018 - 2019","giobd":"6:00","giokt":"9:50","ngay_cap_nhat":"2018-10-01T08:10:32.000+0000"},{"ma_mh":"CH1004","ten_mh":"HOA DAI CUONG (TN)","nhomto":"L36","tuan_hoc":"--|--|--|--|--|--|--|--|--|--|--|--|--|--|48|49|50|51|52|","macoso":"BK-CS2","thu1":2,"tiet_bd1":1,"tiet_kt1":4,"phong1":"H1-502","tc_hp":0,"so_tin_chi":0,"ma_nhom":"L36","mssv":"1812064","hk_nh":"20181","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2018 - 2019","giobd":"6:00","giokt":"9:50","ngay_cap_nhat":"2018-10-01T08:10:32.000+0000"},{"ma_mh":"PE1003","ten_mh":"Gi\u00e1o d\u1ee5c th\u1ec3 ch\u1ea5t 1                                                                                                     ","nhomto":"L32","tuan_hoc":"34|35|36|37|38|39|40|--|--|--|--|45|46|47|48|49|50|51|52|","macoso":"BK-CS2","thu1":3,"tiet_bd1":3,"tiet_kt1":4,"phong1":"CS2-NHATHIDAU-SAN5","tc_hp":1,"so_tin_chi":0,"ma_nhom":"L32","mssv":"1812064","hk_nh":"20181","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2018 - 2019","giobd":"8:00","giokt":"9:50","ngay_cap_nhat":"2018-10-01T08:10:32.000+0000"},{"ma_mh":"CO1006","ten_mh":"NHAP MON DIEN TOAN (TH)","nhomto":"L05","tuan_hoc":"34|35|36|37|38|39|40|--|--|--|--|45|46|47|48|49|50|51|52|","macoso":"BK-CS2","thu1":3,"tiet_bd1":5,"tiet_kt1":6,"phong1":"H6-707","tc_hp":0,"so_tin_chi":0,"ma_nhom":"L05","mssv":"1812064","hk_nh":"20181","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2018 - 2019","giobd":"10:00","giokt":"11:50","ngay_cap_nhat":"2018-10-01T08:10:32.000+0000"},{"ma_mh":"PH1003","ten_mh":"VAT LY 1","nhomto":"L16","tuan_hoc":"34|35|36|37|38|39|40|--|--|--|--|45|46|47|48|49|50|51|52|","macoso":"BK-CS2","thu1":4,"tiet_bd1":7,"tiet_kt1":9,"phong1":"H2-201","tc_hp":4,"so_tin_chi":4,"ma_nhom":"L16","mssv":"1812064","hk_nh":"20181","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2018 - 2019","giobd":"12:00","giokt":"14:50","ngay_cap_nhat":"2018-10-01T08:10:32.000+0000"},{"ma_mh":"MT1003","ten_mh":"GIAI TICH 1","nhomto":"L16","tuan_hoc":"34|35|36|37|38|39|40|--|--|--|--|45|46|47|48|49|50|51|52|","macoso":"BK-CS2","thu1":4,"tiet_bd1":10,"tiet_kt1":12,"phong1":"H2-201","tc_hp":4,"so_tin_chi":4,"ma_nhom":"L16","mssv":"1812064","hk_nh":"20181","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2018 - 2019","giobd":"15:00","giokt":"17:50","ngay_cap_nhat":"2018-10-01T08:10:32.000+0000"},{"ma_mh":"CO1005","ten_mh":"NHAP MON DIEN TOAN","nhomto":"L01","tuan_hoc":"34|35|36|37|38|39|40|--|--|--|--|45|46|47|48|49|50|51|52|","macoso":"BK-CS2","thu1":5,"tiet_bd1":1,"tiet_kt1":2,"phong1":"H1-210","tc_hp":3,"so_tin_chi":3,"ma_nhom":"L01","mssv":"1812064","hk_nh":"20181","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2018 - 2019","giobd":"6:00","giokt":"7:50","ngay_cap_nhat":"2018-10-01T08:10:32.000+0000"},{"ma_mh":"CO1007","ten_mh":"CAUTRUC ROI RAC CHO KHMT","nhomto":"L02","tuan_hoc":"--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|51|52|","macoso":"BK-CS2","thu1":5,"tiet_bd1":3,"tiet_kt1":6,"phong1":"H1-210","tc_hp":4,"so_tin_chi":4,"ma_nhom":"L02","mssv":"1812064","hk_nh":"20181","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2018 - 2019","giobd":"8:00","giokt":"11:50","ngay_cap_nhat":"2018-10-01T08:10:32.000+0000"},{"ma_mh":"CO1007","ten_mh":"CAUTRUC ROI RAC CHO KHMT","nhomto":"L02","tuan_hoc":"34|35|36|37|38|","macoso":"BK-CS2","thu1":5,"tiet_bd1":3,"tiet_kt1":6,"phong1":"H1-210","tc_hp":4,"so_tin_chi":4,"ma_nhom":"L02","mssv":"1812064","hk_nh":"20181","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2018 - 2019","giobd":"8:00","giokt":"11:50","ngay_cap_nhat":"2018-10-01T08:10:32.000+0000"},{"ma_mh":"CO1007","ten_mh":"CAUTRUC ROI RAC CHO KHMT","nhomto":"L02","tuan_hoc":"--|--|--|--|--|--|--|--|--|--|--|45|46|47|48|49|50|","macoso":"BK-CS2","thu1":5,"tiet_bd1":3,"tiet_kt1":6,"phong1":"H1-210","tc_hp":4,"so_tin_chi":4,"ma_nhom":"L02","mssv":"1812064","hk_nh":"20181","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2018 - 2019","giobd":"8:00","giokt":"11:50","ngay_cap_nhat":"2018-10-01T08:10:32.000+0000"},{"ma_mh":"CO1007","ten_mh":"CAUTRUC ROI RAC CHO KHMT","nhomto":"L02","tuan_hoc":"--|--|--|--|--|39|40|","macoso":"BK-CS2","thu1":5,"tiet_bd1":3,"tiet_kt1":6,"phong1":"H1-210","tc_hp":4,"so_tin_chi":4,"ma_nhom":"L02","mssv":"1812064","hk_nh":"20181","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2018 - 2019","giobd":"8:00","giokt":"11:50","ngay_cap_nhat":"2018-10-01T08:10:32.000+0000"},{"ma_mh":"PH1004","ten_mh":"VAT LY 1 (BT)","nhomto":"L31","tuan_hoc":"34|35|36|37|38|39|40|--|--|--|--|45|46|47|48|49|50|51|52|","macoso":"BK-CS2","thu1":6,"tiet_bd1":3,"tiet_kt1":4,"phong1":"H1-403","tc_hp":0,"so_tin_chi":0,"ma_nhom":"L31","mssv":"1812064","hk_nh":"20181","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2018 - 2019","giobd":"8:00","giokt":"9:50","ngay_cap_nhat":"2018-10-01T08:10:32.000+0000"},{"ma_mh":"MT1004","ten_mh":"GIAI TICH 1 (BT)","nhomto":"L31","tuan_hoc":"34|35|36|37|38|39|40|--|--|--|--|45|46|47|48|49|50|51|52|","macoso":"BK-CS2","thu1":6,"tiet_bd1":5,"tiet_kt1":6,"phong1":"H1-403","tc_hp":0,"so_tin_chi":0,"ma_nhom":"L31","mssv":"1812064","hk_nh":"20181","ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2018 - 2019","giobd":"10:00","giokt":"11:50","ngay_cap_nhat":"2018-10-01T08:10:32.000+0000"}],"ten_hocky":"H\u1ecdc k\u1ef3 1 N\u0103m h\u1ecdc 2018 - 2019","hk_nh":"20181","ngay_cap_nhat":"2018-10-01 15:10:32.0"}]"""
            val deserializedResponse: Array<SemesterSchedule> = Gson().fromJson(scheduleResponse, Array<SemesterSchedule>::class.java)
            changeResponse(deserializedResponse)
            selectedSemester.value = deserializedResponse[0]
            isLoading.value = false
        }
    }
}
