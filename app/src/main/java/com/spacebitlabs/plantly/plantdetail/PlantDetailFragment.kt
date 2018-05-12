package com.spacebitlabs.plantly.plantdetail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.spacebitlabs.plantly.R
import com.spacebitlabs.plantly.data.entities.Plant
import com.spacebitlabs.plantly.toBundle
import timber.log.Timber

class PlantDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_plant_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val plantId = arguments?.let {
            val safeArgs = PlantDetailFragmentArgs.fromBundle(it)
            safeArgs.plant_id.toLong()
        } ?: 0

        val model = ViewModelProviders.of(this).get(PlantDetailViewModel::class.java)
        model.plantDetailViewState.observe(this, Observer { state ->
            render(state)
        })

        model.getPlantDetail(plantId)
    }

    private fun render(state: PlantDetailViewState?) {
        when (state) {
            is PlantDetailViewState.PlantDetailLoaded -> renderPlantDetail(state.plant)
        }
    }

    private fun renderPlantDetail(plant: Plant) {
        Timber.d("Rendering plant detail")
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        private const val PLANT_ID: String = "plant_id"

        fun show(view: View, plant: Plant) {
            Navigation.findNavController(view).navigate(R.id.to_plant_detail_action, plant.id.toBundle(PLANT_ID))
        }
    }
}