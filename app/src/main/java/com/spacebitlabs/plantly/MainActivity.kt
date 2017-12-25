package com.spacebitlabs.plantly

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.spacebitlabs.plantly.data.source.UserPlantsStore
import com.spacebitlabs.plantly.plants.PlantsAdapter
import com.spacebitlabs.plantly.plants.PlantsFragment
import kotlinx.android.synthetic.main.fragment_plants.*

/**
 * Home Activity
 */
class MainActivity : AppCompatActivity() {

    private val plantAdapter = PlantsAdapter()
    private val userPlantsStore: UserPlantsStore = UserPlantsStore()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val fragment: Fragment? = supportFragmentManager.findFragmentById(R.id.content_frame)
//        setFragment(fragment)

        plant_list.layoutManager = GridLayoutManager(this, 2)
        plant_list.adapter = plantAdapter

        plantAdapter.setPlantList(userPlantsStore.plants)
    }

    private fun setFragment(newFragment: Fragment?) {
        val fragment: Fragment = newFragment ?: PlantsFragment.newInstance()

//        supportFragmentManager.beginTransaction()
//                .replace(R.id.content_frame, fragment)
//                .commit()
    }
}
