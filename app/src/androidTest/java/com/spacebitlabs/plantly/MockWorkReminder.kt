package com.spacebitlabs.plantly

import com.spacebitlabs.plantly.reminder.WorkReminder

class MockWorkReminder: WorkReminder(Injection.get().providePrefs()) {

    override fun scheduleDailyReminder() {

    }

    override fun cancelDailyReminder() {

    }
}