package com.wonrax.mybk.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val UPDATE_WHEN_START_UP_KEY = "updateWhenStartUp"

object UserSettings {
    // Automatically update to newest data every time the app starts
    val updateWhenStartUp = MutableStateFlow(true)

    init {
        // Listen to settings change and update local storage
        CoroutineScope(Dispatchers.IO).launch {
            updateWhenStartUp.collect {
                EncryptedStorage.setBoolean(UPDATE_WHEN_START_UP_KEY, it)
            }
        }
        updateWhenStartUp.update {
            when (EncryptedStorage.getBoolean(UPDATE_WHEN_START_UP_KEY)) {
                false -> false
                else -> true // either true or null
            }
        }
    }

    fun clear() {
        // Revert back to default value
        updateWhenStartUp.update { true }
    }
}
