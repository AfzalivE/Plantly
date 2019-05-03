package com.spacebitlabs.plantly

import android.app.Application
import com.afzaln.photopicker.PhotoPicker
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.beta.Beta
import com.jakewharton.threetenabp.AndroidThreeTen
import com.squareup.picasso.Picasso
import io.fabric.sdk.android.Fabric
import org.threeten.extra.AndroidThreeTenExtra
import timber.log.Timber

/**
 * App-wide initializations
 */
class PlantlyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Injection.init(this)
        AndroidThreeTen.init(this)
        AndroidThreeTenExtra.init(this)
        PhotoPicker.init(BuildConfig.APPLICATION_ID)

        Fabric.with(this, Beta(), Crashlytics())

        if (BuildConfig.DEBUG) {
            Picasso.get().isLoggingEnabled = true
            Timber.plant(Timber.DebugTree())
        }
    }
}