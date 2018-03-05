package com.spacebitlabs.plantly

import android.app.Application
import timber.log.Timber

/**
 * App-wide initializations
 */
class PlantlyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Injection.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}