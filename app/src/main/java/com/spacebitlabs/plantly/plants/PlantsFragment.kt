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

    val plantAdapter = PlantsAdapter()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.fragment_plants, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val model = ViewModelProviders.of(activity).get(MainViewModel::class.java)
        model.plantListState.observe(this, Observer { state ->
            render(state)
        })

        plant_list.layoutManager = LinearLayoutManager(context)
        plant_list.adapter = plantAdapter

        add_plant.setOnClickListener {
            AddPlantActivity.show(activity)
        }
    }

    private fun render(state: PlantListViewState?) {
        when (state) {
            is PlantListViewState.Loading -> renderLoading(state)
            is PlantListViewState.Empty -> renderEmpty(state)
            is PlantListViewState.PlantsFound -> renderPlantList(state)
        }
    }

    private fun renderPlantList(state: PlantListViewState.PlantsFound) {

    }

    private fun renderEmpty(state: PlantListViewState.Empty) {
        progress.visibility = View.GONE
        plant_list.visibility = View.GONE
        empty.visibility = View.VISIBLE
    }

    private fun renderLoading(state: PlantListViewState.Loading) {
        progress.visibility = View.VISIBLE
        plant_list.visibility = View.GONE
        empty.visibility = View.GONE
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