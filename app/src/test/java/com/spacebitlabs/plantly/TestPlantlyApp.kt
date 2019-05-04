package com.spacebitlabs.plantly

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.squareup.picasso.Picasso

class TestPlantlyApp : PlantlyApp() {

    override fun onCreate() {
        try {
            Picasso.setSingletonInstance(Picasso.Builder(ApplicationProvider.getApplicationContext<Context>()).build())
        } catch (ignored: IllegalStateException) {
        }
        super.onCreate()
    }
}