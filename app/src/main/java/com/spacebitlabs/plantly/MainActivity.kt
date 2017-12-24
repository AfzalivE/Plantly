package com.spacebitlabs.plantly

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.spacebitlabs.plantly.plants.PlantsFragment

/**
 * Home Activity
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment: Fragment? = supportFragmentManager.findFragmentById(R.id.content_frame)
        setFragment(fragment)
    }

    private fun setFragment(newFragment: Fragment?) {
        val fragment: Fragment = newFragment ?: PlantsFragment.newInstance()

        supportFragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit()
    }
}
