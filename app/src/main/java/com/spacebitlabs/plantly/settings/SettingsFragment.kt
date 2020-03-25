package com.spacebitlabs.plantly.settings

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jakewharton.processphoenix.ProcessPhoenix
import com.spacebitlabs.plantly.R
import com.spacebitlabs.plantly.settings.SettingsViewState.BackupCompleteViewState
import com.spacebitlabs.plantly.settings.SettingsViewState.RestoreCompleteViewState
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class SettingsFragment : PreferenceFragmentCompat() {

    private var model: SettingsViewModel? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.app_preferences)
        model = ViewModelProvider(this)[SettingsViewModel::class.java]

        model?.settingsViewState?.observe(this, Observer {
            it?.let { state ->
                render(state)
            }
        })

        val backupPref = findPreference<Preference>("backup")
        backupPref?.setOnPreferenceClickListener {
            runWithPermissions(STORAGE_PERMISSIONS, EXTERNAL_STORAGE_PERMISSION_BACKUP) {
                showBackupFilePicker()
            }
            return@setOnPreferenceClickListener true
        }

        val restorePref = findPreference<Preference>("restore")
        restorePref?.setOnPreferenceClickListener {
            context ?: return@setOnPreferenceClickListener true

            MaterialAlertDialogBuilder(context)
                .setTitle(R.string.restore_warning)
                .setMessage(R.string.restore_warning_message)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    runWithPermissions(STORAGE_PERMISSIONS, EXTERNAL_STORAGE_PERMISSION_RESTORE) {
                        showRestoreFilePicker()
                    }
                }
                .show()

            return@setOnPreferenceClickListener true
        }
    }

    private fun render(viewState: SettingsViewState) {
        when (viewState) {
            is BackupCompleteViewState  -> {
                Toast.makeText(context, R.string.backup_complete, Toast.LENGTH_SHORT).show()
            }
            is RestoreCompleteViewState -> {
                context ?: return
                MaterialAlertDialogBuilder(context)
                    .setTitle(R.string.restore_complete)
                    .setMessage(R.string.restore_complete_message)
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        ProcessPhoenix.triggerRebirth(context)
                    }
                    .show()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (hasPermissions(STORAGE_PERMISSIONS)) {
            when (requestCode) {
                EXTERNAL_STORAGE_PERMISSION_BACKUP  -> showBackupFilePicker()
                EXTERNAL_STORAGE_PERMISSION_RESTORE -> showRestoreFilePicker()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)
        when (requestCode) {
            REQUEST_RESTORE_FILE_PICKER -> if (intentData != null) {
                intentData.data?.let { data ->
                    model?.restore(data)
                }
            }
            REQUEST_BACKUP_FILE_PICKER  -> if (intentData != null) {
                intentData.data?.let { data ->
                    model?.backup(data)
                }
            }
        }
    }

    private fun showBackupFilePicker() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
        val fileName = "plantly_backup_${LocalDateTime.now().format(formatter)}.zip"

        val showFilePickerIntent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            type = "*/*"
            putExtra(Intent.EXTRA_TITLE, fileName)
        }

        startActivityForResult(showFilePickerIntent, REQUEST_BACKUP_FILE_PICKER)
    }

    private fun showRestoreFilePicker() {
        val showFilePickerIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "application/zip"
        }

        startActivityForResult(showFilePickerIntent, REQUEST_RESTORE_FILE_PICKER)
    }

    private fun runWithPermissions(permissions: Array<String>, requestCode: Int, func: () -> Unit) {
        if (!hasPermissions(permissions)) {
            requestPermissions(permissions, requestCode)
        } else {
            func.invoke()
        }
    }

    private fun hasPermissions(permissions: Array<String>): Boolean {
        context ?: return false

        // true if all specified permissions are granted, otherwise false
        return permissions.all { permission ->
            PermissionChecker.checkSelfPermission(context!!, permission) == PERMISSION_GRANTED
        }
    }

    companion object {
        val STORAGE_PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        const val EXTERNAL_STORAGE_PERMISSION_BACKUP = 901
        const val EXTERNAL_STORAGE_PERMISSION_RESTORE = 902

        const val REQUEST_BACKUP_FILE_PICKER = 903
        const val REQUEST_RESTORE_FILE_PICKER = 904

        fun show(navController: NavController) {
            navController.navigate(R.id.to_settings_action)
        }
    }
}