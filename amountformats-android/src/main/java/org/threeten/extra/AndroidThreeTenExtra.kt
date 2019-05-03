package org.threeten.extra

import android.app.Application

object AndroidThreeTenExtra {
    fun init(app: Application) {
        WordBasedProvider(app, "org/threeten/extra/")
    }

}