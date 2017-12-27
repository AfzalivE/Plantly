package com.spacebitlabs.plantly

import android.app.Application
import timber.log.Timber

/**
 * App-wide initializations
 */
class PlantlyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}