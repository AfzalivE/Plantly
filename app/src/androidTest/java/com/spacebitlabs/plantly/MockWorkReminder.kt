package com.spacebitlabs.plantly

import android.content.Context
import com.spacebitlabs.plantly.reminder.WorkReminder

class MockWorkReminder(context: Context): WorkReminder(context, Injection.get().providePrefs()) {

    override fun scheduleDailyReminder() {

    }

    override fun cancelDailyReminder() {

    }
}