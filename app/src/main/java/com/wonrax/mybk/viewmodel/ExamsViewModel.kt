package com.wonrax.mybk.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.wonrax.mybk.model.schedule.SemesterSchedule
import com.wonrax.mybk.repository.ExamsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class ExamsViewModel : ViewModel() {
    private lateinit var repository: ExamsRepository
    lateinit var data: MutableState<Array<SemesterSchedule>?>

    val isLoading = mutableStateOf(true)

    val isRefreshing = mutableStateOf(false)

    val selectedSemester = mutableStateOf<SemesterSchedule?>(null)

    fun constructor(context: Context) {
        this.repository = ExamsRepository(context)
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

        CoroutineScope(Dispatchers.IO).launch {
            try {
                repository.getRemote()
                if (data.value != null) {
                    selectedSemester.value = data.value!![0]
                }
            } catch (e: UnknownHostException) {
                // DO SOMETHING WHEN THERES NO INTERNET CONNECTION
            }
            isLoading.value = false
            isRefreshing.value = false
        }
    }
}
