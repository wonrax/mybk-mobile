package com.wonrax.mybk.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.gson.JsonSyntaxException
import com.wonrax.mybk.model.exam.SemesterExam
import com.wonrax.mybk.model.schedule.SemesterSchedule
import com.wonrax.mybk.repository.ExamsRepository
import com.wonrax.mybk.repository.SchedulesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class MybkViewModel : ViewModel() {

    private lateinit var schedulesRepository: SchedulesRepository
    private lateinit var examsRepository: ExamsRepository

    lateinit var schedulesData: MutableState<Array<SemesterSchedule>?>
    lateinit var examsData: MutableState<Array<SemesterExam>?>

    val isLoading = mutableStateOf(true)

    val isRefreshing = mutableStateOf(false)

    val selectedScheduleSemester = mutableStateOf<SemesterSchedule?>(null)
    val selectedExamSemester = mutableStateOf<SemesterExam?>(null)

    private lateinit var snackBarState: MutableState<SnackBarState>

    fun constructor(context: Context, snackBarState: MutableState<SnackBarState>) {

        schedulesRepository = SchedulesRepository(context)
        examsRepository = ExamsRepository(context)

        this.snackBarState = snackBarState

        schedulesData = schedulesRepository.data
        examsData = examsRepository.data

        val isSchedulesCached = this.schedulesRepository.getLocal()
        val isExamsCached = this.schedulesRepository.getLocal()

        if (isExamsCached && isSchedulesCached) {
            selectedScheduleSemester.value = schedulesData.value!![0]
            isLoading.value = false
        }
        update()
    }

    fun update() {
        if (!isLoading.value) isRefreshing.value = true

        CoroutineScope(IO).launch {
            try {
                // TODO make this concurrent
                schedulesRepository.getRemote()
                examsRepository.getRemote()
                if (schedulesData.value != null) {
                    selectedScheduleSemester.value = schedulesData.value!![0]
                }
                if (examsData.value != null) {
                    selectedExamSemester.value = examsData.value!![0]
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
