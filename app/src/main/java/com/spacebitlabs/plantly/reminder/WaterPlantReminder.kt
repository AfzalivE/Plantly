package com.spacebitlabs.plantly.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.spacebitlabs.plantly.Injection
import com.spacebitlabs.plantly.NavigationActivity
import com.spacebitlabs.plantly.R
import com.spacebitlabs.plantly.data.entities.Plant
import kotlinx.coroutines.runBlocking
import org.threeten.bp.OffsetDateTime
import timber.log.Timber

class WaterPlantReminder(context: Context, params: WorkerParameters) : Worker(
    context,
    params
) {
    override fun doWork(): Result {
        Injection.get().providePrefs().saveWorkTime()
        resetIfWrongTime()
        notifyUser()
        return Result.success()
    }

    fun isWrongTime(now: OffsetDateTime): Boolean {
        return now.isAfter(now.withHour(12)) || now.isBefore(now.withHour(9))
    }

    private fun resetIfWrongTime() {
        if (isWrongTime(OffsetDateTime.now())) {
            Injection.get().provideWorkReminder().resetDailyReminder()
        }
    }

    companion object {
        private const val CHANNEL_ID = "default"
        private const val REMINDER_NOTIFICATION_ID = 21
        private const val REMINDER_REQUEST_CODE = 11


        fun notifyUser() {
            runBlocking {
                // check actual watering times to notify the user
                val plantsToWater = Injection.get().providePlantStore().getPlantsToWaterToday()
                if (plantsToWater.isNotEmpty()) {
                    notifyUserForPlants(plantsToWater)
                }
            }
        }

        private fun notifyUserForPlants(plants: List<Plant>) {
            val context = Injection.get().provideContext()

            Timber.d("Got ${plants.size} to water")

            val quantityText = context.resources.getQuantityText(R.plurals.plants_need_watering, plants.size)
            val textWithPlants = if (plants.size > 2) {
                "${plants.size} plants $quantityText"
            } else {
                plants.joinToString(separator = " and ", postfix = " $quantityText", transform = Plant::name)
            }

            val contentIntent = PendingIntent.getActivity(
                context,
                REMINDER_REQUEST_CODE,
                Intent(context, NavigationActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                },
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_sprout_outline)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
                .setContentTitle("Plantly Reminder")
                .setContentText(textWithPlants)
                .setContentIntent(contentIntent)
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
    }
}
