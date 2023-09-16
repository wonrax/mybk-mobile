package com.wonrax.mybk.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.gson.JsonSyntaxException
import com.wonrax.mybk.model.SnackbarManager
import com.wonrax.mybk.model.exam.SemesterExam
import com.wonrax.mybk.model.grade.SemesterGrade
import com.wonrax.mybk.model.schedule.SemesterSchedule
import com.wonrax.mybk.repository.ExamsRepository
import com.wonrax.mybk.repository.GradesRepository
import com.wonrax.mybk.repository.SchedulesRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class MybkViewModel(
    private val schedulesRepository: SchedulesRepository,
    private val examsRepository: ExamsRepository,
    private val gradesRepository: GradesRepository,
    private val snackbarManager: SnackbarManager
) {

    // TODO make these read only states
    val schedulesData: MutableState<Array<SemesterSchedule>?> = schedulesRepository.data
    val examsData: MutableState<Array<SemesterExam>?> = examsRepository.data
    val gradesData: MutableState<Array<SemesterGrade>?> = gradesRepository.data

    val isLoading = mutableStateOf(true)

    val isRefreshing = mutableStateOf(true)

    // The selected semester to view on the screen
    val selectedScheduleSemester = mutableStateOf<SemesterSchedule?>(null)
    val selectedExamSemester = mutableStateOf<SemesterExam?>(null)
    val selectedGradeSemester = mutableStateOf<SemesterGrade?>(null)

    // If there are data cached in local storage
    var isDataCached = false

    init {
        // Get cached data
        val isSchedulesCached = this.schedulesRepository.getLocal()
        val isExamsCached = this.examsRepository.getLocal()
        val isGradesCached = this.gradesRepository.getLocal()

        isDataCached = isExamsCached && isSchedulesCached && isGradesCached

        if (isDataCached) {
            selectedScheduleSemester.value = schedulesData.value!![0]
            selectedExamSemester.value = examsData.value!![0]
            selectedGradeSemester.value = gradesData.value!![0]
            isLoading.value = false
        }
    }

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        isLoading.value = false
        isRefreshing.value = false
        when (exception) {
            is UnknownHostException -> {
                snackbarManager.showMessage("Không thể kết nối. Đang hiển thị dữ liệu cũ.")
            }
            is SocketTimeoutException -> {
                snackbarManager.showMessage("Không thể kết nối. Đang hiển thị dữ liệu cũ.")
            }
            is JsonSyntaxException -> {
                snackbarManager.showMessage("Lỗi xử lý dữ liệu: Dữ liệu trả về không đúng định dạng.")
            }
            else -> {
                snackbarManager.showMessage(exception.localizedMessage ?: "Lỗi không xác định")
            }
        }
    }

    fun update() {
        if (!isLoading.value) isRefreshing.value = true

        CoroutineScope(Dispatchers.IO).launch(coroutineExceptionHandler) {
            val jobs = listOf(
                async {
                    schedulesRepository.getRemote()
                    if (schedulesData.value != null) {
                        selectedScheduleSemester.value = schedulesData.value!![0]
                    }
                },
                async {
                    examsRepository.getRemote()
                    if (examsData.value != null) {
                        selectedExamSemester.value = examsData.value!![0]
                    }
                },
                async {
                    gradesRepository.getRemote()
                    if (gradesData.value != null) {
                        selectedGradeSemester.value = gradesData.value!![0]
                    }
                }
            )

            jobs.awaitAll()

            isLoading.value = false
            isRefreshing.value = false
        }
    }

    fun invalidateLocalStorage() {
        // Delete all data on local storage
        schedulesRepository.localStore(null)
        examsRepository.localStore(null)
        gradesRepository.localStore(null)
    }
}
