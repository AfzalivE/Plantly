package com.spacebitlabs.plantly.plants

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.transition.TransitionManager
import androidx.fragment.app.Fragment
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.spacebitlabs.plantly.data.entities.Plant
import kotlinx.android.synthetic.main.fragment_plants.*
import kotlinx.android.synthetic.main.layout_appbar.*


class PlantsFragment : androidx.fragment.app.Fragment() {

    private val plantAdapter = PlantsAdapter()
    private val todayAdapter = TodayAdapter()
    private var model: PlantsViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(com.spacebitlabs.plantly.R.layout.fragment_plants, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        renderEmpty()

        model = ViewModelProviders.of(this).get(PlantsViewModel::class.java)
        model?.plantListState?.observe(this, Observer { state ->
            render(state)
        })

        plant_list.layoutManager = androidx.recyclerview.widget.GridLayoutManager(activity, 2)
        plant_list.adapter = plantAdapter

        today_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        today_list.adapter = todayAdapter

        add_plant.setOnClickListener(Navigation.createNavigateOnClickListener(com.spacebitlabs.plantly.R.id.to_add_plants_action))

        val offsetChangeListener = OffsetChangeListener()
        appbar.addOnOffsetChangedListener(offsetChangeListener)
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
            is PlantListViewState.PlantsFound -> {
                renderPlantList(state)
                if (state.todayPlants.isEmpty()) {
                    renderEmptyToday()
                } else {
                    renderToday(state.todayPlants)
                }
            }
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
    }

    private fun renderEmptyToday() {
        today_title.text = "Good Morning"
        today_subtitle.text = "No plants to water today!"
    }

    private fun renderToday(todayPlants: List<Plant>) {
        // TODO get actual list of plants to water today
        today_title.text = "Good Morning"
        today_subtitle.text = "${todayPlants.size} plants to water today"
        todayAdapter.setPlantList(todayPlants)
    }

    private fun lockAppBar() {
        appbar.setExpanded(false)
        ViewCompat.setNestedScrollingEnabled(plant_list, false)
        val params = appbar.layoutParams as androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
        if (params.behavior == null) {
            params.behavior = AppBarLayout.Behavior()
        }

        val behaviour = params.behavior as AppBarLayout.Behavior
        behaviour.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
            override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                return false
            }
        })
    }

    private fun unlockAppBar() {
        appbar.setExpanded(true)
        ViewCompat.setNestedScrollingEnabled(plant_list, true)
        val params = appbar.layoutParams as androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
        if (params.behavior == null) {
            params.behavior = AppBarLayout.Behavior()
        }

        val behaviour = params.behavior as AppBarLayout.Behavior
        behaviour.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
            override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                return true
            }
        })
    }
}