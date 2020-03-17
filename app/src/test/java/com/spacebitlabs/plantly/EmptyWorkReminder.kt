package com.spacebitlabs.plantly

import com.spacebitlabs.plantly.reminder.WorkReminder

class EmptyWorkReminder : WorkReminder(
    Injection.get().provideContext(),
    Injection.get().providePrefs()
) {

    override fun scheduleDailyReminder() {

    }

    override fun cancelDailyReminder() {

    }
}