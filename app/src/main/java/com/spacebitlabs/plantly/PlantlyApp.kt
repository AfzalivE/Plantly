package com.spacebitlabs.plantly

import android.app.Application

/**
 * App-wide initializations
 */
class PlantlyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {

        }
    }
}