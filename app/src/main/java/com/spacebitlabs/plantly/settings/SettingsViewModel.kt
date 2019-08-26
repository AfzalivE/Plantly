package com.spacebitlabs.plantly.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.spacebitlabs.plantly.Injection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {

    private val viewModelJob = Job()

    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val backupManager by lazy {
        Injection.get().provideBackupManager()
    }

    fun backup() {
        viewModelScope.launch {
            // TODO show progress bar
            backupManager.backup()
        }
    }

    fun restore(fileUri: Uri) {
        viewModelScope.launch {
            // TODO show progress bar
            backupManager.restore(fileUri)
        }
    }

    override fun onCleared() {
        super.onCleared()

    }
}
