package com.spacebitlabs.plantly

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * WorkManager does not maintain WorkRequests
 * between app updates so we need to schedule
 * them again.
 */
class UpdateReceiver : BroadcastReceiver() {

    private val receiverJob = Job()

    private val receiverScope = CoroutineScope(Dispatchers.Main + receiverJob)

    override fun onReceive(context: Context, intent: Intent?) {
        Timber.d("Update detected: Rescheduling Work Reminders")
        receiverScope.launch {
            if (Injection.get().providePlantStore().getAllPlants().isNotEmpty()) {
                Injection.get().provideWorkReminder().scheduleDailyReminder()
                Timber.d("Update detected: Done rescheduling Work Reminders")
            }
        }
    }
}
