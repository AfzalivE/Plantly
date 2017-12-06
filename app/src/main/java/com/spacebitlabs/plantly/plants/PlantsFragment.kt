package com.spacebitlabs.plantly.plants

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spacebitlabs.plantly.MainViewModel
import com.spacebitlabs.plantly.R
import com.spacebitlabs.plantly.addplant.AddPlantActivity
import kotlinx.android.synthetic.main.fragment_plants.*

/**
 * Shows the list of plants a user has.
 *
 * User can click the add FAB and add more plants.
 */
class PlantsFragment : Fragment() {

    private val plantAdapter = PlantsAdapter()
    private var model: MainViewModel? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.fragment_plants, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderEmpty()

        model = ViewModelProviders.of(activity).get(MainViewModel::class.java)
        model?.plantListState?.observe(this, Observer { state ->
            render(state)
        })

        plant_list.layoutManager = LinearLayoutManager(context)
        plant_list.adapter = plantAdapter

        add_plant.setOnClickListener {
            AddPlantActivity.show(activity)
        }
    }

    override fun onResume() {
        super.onResume()
        model?.loadUserPlants()
    }

    private fun render(state: PlantListViewState?) {
        when (state) {
            is PlantListViewState.Loading -> renderLoading()
            is PlantListViewState.Empty -> renderEmpty()
            is PlantListViewState.Error -> renderError()
            is PlantListViewState.PlantsFound -> renderPlantList(state)
        }
    }

    private fun renderLoading() {
        progress.visibility = View.VISIBLE
        plant_list.visibility = View.GONE
        empty.visibility = View.GONE
        error.visibility = View.GONE
    }

    private fun renderEmpty() {
        progress.visibility = View.GONE
        plant_list.visibility = View.GONE
        empty.visibility = View.VISIBLE
        error.visibility = View.GONE
    }

    private fun renderError() {
        progress.visibility = View.GONE
        plant_list.visibility = View.GONE
        empty.visibility = View.GONE
        error.visibility = View.VISIBLE
    }

    private fun renderPlantList(state: PlantListViewState.PlantsFound) {
        progress.visibility = View.GONE
        plant_list.visibility = View.VISIBLE
        empty.visibility = View.GONE
        error.visibility = View.GONE
        plantAdapter.setPlantList(state.plants)
    }

    companion object {
        fun newInstance(): PlantsFragment {
            val args = Bundle().apply { /* put params */ }
            val fragment = PlantsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}