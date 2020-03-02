package com.spacebitlabs.plantly.data

import android.content.Context
import android.content.SharedPreferences
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

open class Prefs(private val appContext: Context) {

    companion object {
        const val PREFS_NAME = "plantly_prefs"
        const val WORK_REMINDER_ID = "work_reminder_id"
        const val WORK_TIME = "work_time"
    }

    private fun getSharedPrefs(): SharedPreferences = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setWorkReminderId(id: UUID) {
        getSharedPrefs().edit().putString(WORK_REMINDER_ID, id.toString()).apply()
    }

    val workReminderId: String
        get() = getSharedPrefs().getString(WORK_REMINDER_ID, "")!!

    fun saveWorkTime() {
        getSharedPrefs().edit().putString(WORK_TIME, OffsetDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME).toString()).apply()
    }
}