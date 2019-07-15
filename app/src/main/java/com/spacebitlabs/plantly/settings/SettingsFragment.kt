package com.spacebitlabs.plantly.settings

import android.Manifest
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.spacebitlabs.plantly.R

class SettingsFragment : PreferenceFragmentCompat() {

    private var model: SettingsViewModel? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.app_preferences)
        model = ViewModelProviders.of(this).get(SettingsViewModel::class.java)

        val backupPref = findPreference<Preference>("backup")
        backupPref?.setOnPreferenceClickListener {
            runWithPermissions(STORAGE_PERMISSIONS, EXTERNAL_STORAGE_PERMISSION_BACKUP) {
                model?.backup()
            }
            return@setOnPreferenceClickListener true
        }

        val restorePref = findPreference<Preference>("restore")
        restorePref?.setOnPreferenceClickListener {
            runWithPermissions(STORAGE_PERMISSIONS, EXTERNAL_STORAGE_PERMISSION_RESTORE) {
                model?.restore()
            }
            return@setOnPreferenceClickListener true
        }
    }

    private fun runWithPermissions(permissions: Array<String>, requestCode: Int, func: () -> Unit) {
        if (!hasPermissions(permissions)) {
            requestPermissions(permissions, requestCode)
        } else {
            func.invoke()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (hasPermissions(STORAGE_PERMISSIONS)) {
            when (requestCode) {
                EXTERNAL_STORAGE_PERMISSION_BACKUP -> model?.backup()
                EXTERNAL_STORAGE_PERMISSION_RESTORE -> model?.restore()
            }
        }
    }

    private fun requestPermission(permissions: Array<String>, requestCode: Int) {
        activity ?: return
        ActivityCompat.requestPermissions(activity!!, permissions, requestCode)
    }

    private fun hasPermissions(permissions: Array<String>): Boolean {
        context ?: return true

        // true if all specified permissions are granted, otherwise false
        return permissions.all { permission ->
            PermissionChecker.checkSelfPermission(context!!, permission) == PERMISSION_GRANTED
        }
    }

    companion object {
        val STORAGE_PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        const val EXTERNAL_STORAGE_PERMISSION_BACKUP = 901
        const val EXTERNAL_STORAGE_PERMISSION_RESTORE = 902

        fun show(navController: NavController) {
            navController.navigate(R.id.to_settings_action)
        }
    }
}