package com.spacebitlabs.plantly.settings

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.spacebitlabs.plantly.Injection
import kotlinx.coroutines.*

class SettingsViewModel : ViewModel() {

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val backupManager by lazy {
        Injection.get().provideBackupManager()
    }

    val settingsViewState: MutableLiveData<SettingsViewState> = MutableLiveData()

    fun backup(fileUri: Uri) {
        viewModelScope.launch {
            // TODO show progress bar
            backupManager.backup(fileUri)
            settingsViewState.postValue(SettingsViewState.BackupCompleteViewState)
        }
    }

    fun restore(fileUri: Uri) {
        viewModelScope.launch {
            // TODO show progress bar
            backupManager.restore(fileUri)
            settingsViewState.postValue(SettingsViewState.RestoreCompleteViewState)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}

sealed class SettingsViewState {
    object BackupCompleteViewState : SettingsViewState()
    object RestoreCompleteViewState : SettingsViewState()
}
