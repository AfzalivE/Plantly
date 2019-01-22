package com.spacebitlabs.plantly.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.spacebitlabs.plantly.Injection
import com.spacebitlabs.plantly.R

class WaterPlantReminder(private val params: WorkerParameters) : Worker(
    Injection.get().provideContext(),
    params
) {
    override fun doWork(): Result {
        notifyUser()
        return Result.success()
    }

    private fun notifyUser() {
        val context = Injection.get().provideContext()

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Plantly Reminder")
            .setContentText("Water your plants!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        createNotificationChannel(context)

        // notificationId is a unique int for each notification that you must define
        val notificationId = REMINDER_NOTIFICATION_ID

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, notificationBuilder.build())
        }
    }

    private fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "default"
        const val REMINDER_NOTIFICATION_ID = 1345
    }
}