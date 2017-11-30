package com.spacebitlabs.plantly

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.spacebitlabs.plantly.calendar.CalendarFragment
import com.spacebitlabs.plantly.plants.PlantsFragment
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Home Activity
 */
class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                val fragment = CalendarFragment.newInstance()
                setFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                val fragment = PlantsFragment.newInstance()
                setFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                val fragment = CalendarFragment.newInstance()
                setFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val fragment: Fragment? = supportFragmentManager.findFragmentById(R.id.content_frame)
        setFragment(fragment)
    }

    private fun setFragment(newFragment: Fragment?) {
        val fragment: Fragment = newFragment ?: CalendarFragment.newInstance()

        supportFragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit()
    }
}
