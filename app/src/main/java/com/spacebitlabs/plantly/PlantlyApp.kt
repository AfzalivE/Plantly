package com.spacebitlabs.plantly

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.squareup.picasso.Picasso
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
            Picasso.get().isLoggingEnabled = true
            Timber.plant(Timber.DebugTree())
        }
    }
}