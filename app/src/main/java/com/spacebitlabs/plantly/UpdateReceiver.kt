package com.spacebitlabs.plantly

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.WorkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Reset daily reminder scheduler on Update
 * mainly to accomodate any changes in
 * the way daily reminders work in the app.
 *
 * For existing scheduled reminders.
 */
class UpdateReceiver : BroadcastReceiver() {

    private val receiverJob = Job()

    private val receiverScope = CoroutineScope(Dispatchers.Main + receiverJob)

    override fun onReceive(context: Context, intent: Intent?) {
        Timber.d("Update detected: Rescheduling Work Reminders")
        receiverScope.launch {
            if (Injection.get().providePlantStore().getAllPlants().isNotEmpty()) {
                WorkManager.getInstance().cancelAllWork()
                Injection.get().provideWorkReminder().scheduleDailyReminder()
                Timber.d("Update detected: Done rescheduling Work Reminders")
            }
        }
    }
}
