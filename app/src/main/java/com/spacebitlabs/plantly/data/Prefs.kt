package com.spacebitlabs.plantly.data

import android.content.Context
import android.content.SharedPreferences
import java.util.*

class Prefs(private val appContext: Context) {

    companion object {
        const val PREFS_NAME = "plantly_prefs"
        const val WORK_REMINDER_ID = "work_reminder_id"
    }

    private fun getSharedPrefs(): SharedPreferences {
        return appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun setWorkReminderId(id: UUID) {
        getSharedPrefs().edit().putString(WORK_REMINDER_ID, id.toString()).apply()
    }

    fun getWorkReminderId(): String = getSharedPrefs().getString(WORK_REMINDER_ID, "")!!

}