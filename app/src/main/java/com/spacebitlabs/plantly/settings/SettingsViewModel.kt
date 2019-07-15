package com.spacebitlabs.plantly.settings

import androidx.lifecycle.ViewModel
import com.spacebitlabs.plantly.Injection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {

    private val viewModelJob = Job()

    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val userPlantsStore by lazy {
        Injection.get().providePlantStore()
    }

    fun backup() {
        viewModelScope.launch {
            // TODO show progress bar
            userPlantsStore.backup()
        }
    }

    fun restore() {
        viewModelScope.launch {
            // TODO show progress bar
            userPlantsStore.restore()
        }
    }

    override fun onCleared() {
        super.onCleared()

    }
}
