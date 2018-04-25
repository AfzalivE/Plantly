package com.spacebitlabs.plantly.plants

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.transition.TransitionManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.spacebitlabs.plantly.R
import com.spacebitlabs.plantly.addplant.AddPlantActivity
import kotlinx.android.synthetic.main.activity_plants.*

/**
 * Home Activity
 */
class PlantsActivity : AppCompatActivity() {

    private val plantAdapter = PlantsAdapter()
    private val todayAdapter = TodayAdapter()
    private var model: PlantsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plants)

        renderEmpty()

        model = ViewModelProviders.of(this).get(PlantsViewModel::class.java)
        model?.plantListState?.observe(this, Observer { state ->
            render(state)
        })

        plant_list.layoutManager = GridLayoutManager(this, 2)
        plant_list.adapter = plantAdapter

        today_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        today_list.adapter = todayAdapter

        add_plant.setOnClickListener {
            AddPlantActivity.show(this)
        }
    }

    override fun onResume() {
        super.onResume()
        model?.loadUserPlants()
    }

    private fun render(state: PlantListViewState?) {
        when (state) {
            is PlantListViewState.Loading     -> renderLoading()
            is PlantListViewState.Empty       -> renderEmpty()
            is PlantListViewState.Error       -> renderError()
            is PlantListViewState.PlantsFound -> renderPlantList(state)
        }
    }

    private fun renderLoading() {
        TransitionManager.beginDelayedTransition(container)
        progress.visibility = View.VISIBLE
        plant_list.visibility = View.GONE
        empty.visibility = View.GONE
        error.visibility = View.GONE
    }

    private fun renderEmpty() {
        TransitionManager.beginDelayedTransition(container)
        progress.visibility = View.GONE
        plant_list.visibility = View.GONE
        empty.visibility = View.VISIBLE
        error.visibility = View.GONE
    }

    private fun renderError() {
        TransitionManager.beginDelayedTransition(container)
        progress.visibility = View.GONE
        plant_list.visibility = View.GONE
        empty.visibility = View.GONE
        error.visibility = View.VISIBLE
    }

    private fun renderPlantList(state: PlantListViewState.PlantsFound) {
        TransitionManager.beginDelayedTransition(container)
        progress.visibility = View.GONE
        plant_list.visibility = View.VISIBLE
        empty.visibility = View.GONE
        error.visibility = View.GONE
        plantAdapter.setPlantList(state.plants)
        // TODO get actual list of plants to water today
        todayAdapter.setPlantList(state.plants)
    }
}
