package com.spacebitlabs.plantly

import androidx.test.core.app.ApplicationProvider
import com.squareup.picasso.Picasso

class TestPlantlyApp : PlantlyApp() {

    override fun onCreate() {
        try {
            Picasso.setSingletonInstance(Picasso.Builder(ApplicationProvider.getApplicationContext()).build())
        } catch (ignored: IllegalStateException) {
        }
        super.onCreate()
    }
}