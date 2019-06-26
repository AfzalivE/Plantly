package com.spacebitlabs.plantly.settings

import android.os.Bundle
import androidx.navigation.NavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.spacebitlabs.plantly.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.app_preferences)
        val backupPref = findPreference<Preference>("backup")
        backupPref?.setOnPreferenceClickListener {
            
            return@setOnPreferenceClickListener true
        }
    }

    companion object {
        fun show(navController: NavController) {
            navController.navigate(R.id.to_settings_action)
        }
    }
}