package com.spacebitlabs.plantly

import com.spacebitlabs.plantly.Injection
import com.spacebitlabs.plantly.reminder.WorkReminder

class EmptyWorkReminder: WorkReminder(Injection.get().providePrefs()) {

    override fun scheduleDailyReminder() {

    }

    override fun cancelDailyReminder() {

    }
}