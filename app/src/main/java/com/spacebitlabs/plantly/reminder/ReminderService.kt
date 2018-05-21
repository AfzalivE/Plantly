package com.spacebitlabs.plantly.reminder

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.spacebitlabs.plantly.Injection
import timber.log.Timber

/**
 * Created by afzal_najam on 2018-02-18.
 */
class ReminderService : Service() {

    val plantStore = Injection.get().providePlantStore()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        Timber.d("Service created")
        super.onCreate()
    }

    override fun onDestroy() {
        Timber.d("Service destroyed")
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("Service started")
        return super.onStartCommand(intent, flags, startId)
    }
}