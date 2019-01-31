package com.spacebitlabs.plantly.reminder

import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.spacebitlabs.plantly.data.Prefs
import java.util.*
import java.util.concurrent.TimeUnit

open class WorkReminder(private val prefs: Prefs) {

    open fun cancelDailyReminder() {
        val workReminderId = prefs.getWorkReminderId()
        if (workReminderId.isNotEmpty()) {
            val workReminderUuid = UUID.fromString(workReminderId)
            WorkManager.getInstance().cancelWorkById(workReminderUuid)
        }
    }

    open fun scheduleDailyReminder() {
        val workReminder = PeriodicWorkRequestBuilder<WaterPlantReminder>(1, TimeUnit.DAYS).build()
        prefs.setWorkReminderId(workReminder.id)
        WorkManager.getInstance().enqueue(workReminder)
    }
}