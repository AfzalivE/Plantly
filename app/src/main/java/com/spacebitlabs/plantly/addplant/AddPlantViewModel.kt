package com.spacebitlabs.plantly.addplant

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.spacebitlabs.plantly.Injection
import com.spacebitlabs.plantly.data.entities.Plant

/**
 * ViewModel for Add Plant screen
 */
class AddPlantViewModel : ViewModel() {

    private val userPlantsStore by lazy {
        Injection.get().providePlantStore()
    }

    internal val addPlantViewState: MutableLiveData<AddPlantViewState> = MutableLiveData()

    fun requestSuggestions(name: String) {
        addPlantViewState.value = AddPlantViewState.Loading()

        // TODO get suggestions from server then set suggestions value
        val plants = ArrayList<Plant>()
        plants.add(Plant(10, "Sansa"))

        addPlantViewState.value = AddPlantViewState.SuggestionsFound(plants)
    }

    fun addPlant(plant: Plant) {
        userPlantsStore.addPlant(plant)
    }
}