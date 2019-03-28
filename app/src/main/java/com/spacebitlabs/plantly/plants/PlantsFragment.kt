package com.spacebitlabs.plantly.plants

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.selection.*
import androidx.recyclerview.selection.SelectionTracker.SelectionObserver
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import com.google.android.material.appbar.AppBarLayout
import com.spacebitlabs.plantly.R
import com.spacebitlabs.plantly.R.layout
import com.spacebitlabs.plantly.data.entities.Plant
import com.spacebitlabs.plantly.reminder.WaterPlantReminder
import kotlinx.android.synthetic.main.fragment_plants.*
import kotlinx.android.synthetic.main.layout_appbar.*


class PlantsFragment : Fragment() {

    private val plantAdapter = PlantsAdapter()
    private val todayAdapter = TodayAdapter()
    private var model: PlantsViewModel? = null
    private var selectionTracker: SelectionTracker<Long>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(layout.fragment_plants, container, false)

    private val plantSelectionObserver = object : SelectionObserver<Long>() {
        override fun onSelectionChanged() {
            switchFabIcon(selectionTracker?.hasSelection() == true, selectionTracker?.selection)
        }
    }

    private fun switchFabIcon(showDeleteIcon: Boolean, selection: Selection<Long>?) {
        val icon = if (showDeleteIcon) {
            add_plant.setOnClickListener {
                model?.deletePlants(selection?.toList() ?: emptyList())
            }
            R.drawable.plus_to_bin
        } else {
            add_plant.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.to_add_plants_action))
            R.drawable.bin_to_plus
        }

        // TODO check if already showing the bin icon and don't animate again
        add_plant.setImageResource(icon)
        (add_plant.drawable as AnimatedVectorDrawable).start()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        renderEmpty()

        model = ViewModelProviders.of(this).get(PlantsViewModel::class.java)
        model?.plantListState?.observe(this, Observer { state ->
            render(state)
        })

        plant_list.layoutManager = GridLayoutManager(activity, 2)
        plant_list.adapter = plantAdapter

        today_list.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        today_list.adapter = todayAdapter

        selectionTracker = SelectionTracker.Builder<Long>(
            "plantSelectionId",
            plant_list,
            StableIdKeyProvider(plant_list),
            PlantItemDetailsLookup(plant_list),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        selectionTracker?.addObserver(plantSelectionObserver)

        plantAdapter.tracker = selectionTracker

        add_plant.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.to_add_plants_action))
        add_plant.setOnLongClickListener {
            WaterPlantReminder.notifyUser()
            return@setOnLongClickListener true
        }

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
        val params = appbar.layoutParams as CoordinatorLayout.LayoutParams
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
        val params = appbar.layoutParams as CoordinatorLayout.LayoutParams
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