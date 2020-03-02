package com.spacebitlabs.plantly

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes


class NavigationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

//        main_collapsing.title = getString(R.string.app_name)

        if (!BuildConfig.DEBUG) {
            AppCenter.start(
                application, BuildConfig.APP_CENTER_APP_SECRET,
                Analytics::class.java, Crashes::class.java
            )
        }
    }
}