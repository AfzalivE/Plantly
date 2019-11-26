package com.spacebitlabs.plantly.reminder

import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.spacebitlabs.plantly.data.Prefs
import org.threeten.bp.OffsetDateTime
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

open class WorkReminder(private val context: Context, private val prefs: Prefs) {

    open fun cancelDailyReminder() {
        val workReminderId = prefs.getWorkReminderId()
        if (workReminderId.isNotEmpty()) {
            val workReminderUuid = UUID.fromString(workReminderId)
            WorkManager.getInstance(context).cancelWorkById(workReminderUuid)
        }
    }

    open fun scheduleDailyReminder() {
        if (prefs.getWorkReminderId().isNotEmpty()) {
            val workInfoListenable = WorkManager.getInstance(context).getWorkInfoById(UUID.fromString(prefs.getWorkReminderId()))
            // if work already exists, don't schedule another reminder
            if (workInfoListenable.get() != null) return
        }

        val initialDelay = getInitialDelay(OffsetDateTime.now())

        val workReminder = PeriodicWorkRequestBuilder<WaterPlantReminder>(1, TimeUnit.DAYS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()
        Timber.d("Scheduling work reminder")
        prefs.setWorkReminderId(workReminder.id)
        WorkManager.getInstance(context).enqueue(workReminder)
    }

    fun getInitialDelay(now: OffsetDateTime): Long {
        val isAfter11Am = now.isAfter(now.withHour(11))

        return if (isAfter11Am) {
            val nextMorning = now.plusDays(1).withHour(11).withMinute(0).withSecond(0)
            nextMorning.toInstant().toEpochMilli() - now.toInstant().toEpochMilli()
        } else {
            val thisMorning = now.withHour(11).withMinute(0).withSecond(0)
            thisMorning.toInstant().toEpochMilli() - now.toInstant().toEpochMilli()
        }
    }
}