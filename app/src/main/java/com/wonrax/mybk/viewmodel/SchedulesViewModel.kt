package com.wonrax.mybk.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.gson.JsonSyntaxException
import com.wonrax.mybk.model.schedule.SemesterSchedule
import com.wonrax.mybk.repository.SchedulesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class SchedulesViewModel : ViewModel() {
    private lateinit var repository: SchedulesRepository
    lateinit var data: MutableState<Array<SemesterSchedule>?>

    val isLoading = mutableStateOf(true)

    val isRefreshing = mutableStateOf(false)

    val selectedSemester = mutableStateOf<SemesterSchedule?>(null)

    private lateinit var snackBarState: MutableState<SnackBarState>

    fun constructor(context: Context, snackBarState: MutableState<SnackBarState>) {
        this.repository = SchedulesRepository(context)
        this.snackBarState = snackBarState
        data = this.repository.data
        val isCached = this.repository.getLocal()
        if (isCached) {
            selectedSemester.value = data.value!![0]
            isLoading.value = false
        }
        update()
    }

    fun update() {
        if (!isLoading.value) isRefreshing.value = true

        CoroutineScope(IO).launch {
            try {
                repository.getRemote()
                if (data.value != null) {
                    selectedSemester.value = data.value!![0]
                }
            } catch (e: UnknownHostException) {
                // DO SOMETHING WHEN THERES NO INTERNET CONNECTION
            } catch (e: JsonSyntaxException) {
                snackBarState.value = SnackBarState(
                    true,
                    "Lỗi xử lý dữ liệu: Dữ liệu trả về không đúng định dạng."
                )
            } catch (e: Exception) {
                snackBarState.value = SnackBarState(true, e.message)
            }
            isLoading.value = false
            isRefreshing.value = false
        }
    }
}
