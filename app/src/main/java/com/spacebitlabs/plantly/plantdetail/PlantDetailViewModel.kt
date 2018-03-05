package com.spacebitlabs.plantly.plantdetail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.spacebitlabs.plantly.Injection

/**
 * ViewModel for Add Plant screen
 */
class PlantDetailViewModel : ViewModel() {

    private val userPlantsStore by lazy {
        Injection.get().providePlantStore()
    }

    val plantDetailViewState: MutableLiveData<PlantDetailViewState> = MutableLiveData()

    fun getPlantDetail(plantId: Long) {
        val plant = userPlantsStore.getPlant(plantId)
        plantDetailViewState.value = PlantDetailViewState.PlantDetailLoaded(plant)
        // TODO show other properties like how many watering/soil events, birthdate, christening, etc
    }
}