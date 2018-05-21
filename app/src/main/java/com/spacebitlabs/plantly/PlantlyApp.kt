package com.spacebitlabs.plantly

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import timber.log.Timber

/**
 * App-wide initializations
 */
class PlantlyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Injection.init(this)
        AndroidThreeTen.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}