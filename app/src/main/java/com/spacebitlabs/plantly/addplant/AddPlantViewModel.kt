package com.spacebitlabs.plantly.addplant

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.spacebitlabs.plantly.data.models.Plant

/**
 * ViewModel for Add Plant screen
 */
class AddPlantViewModel : ViewModel() {

    val addPlantViewState: MutableLiveData<AddPlantViewState> = MutableLiveData()

    fun requestSuggestions(name: String) {
        addPlantViewState.value = AddPlantViewState.Loading()

        // TODO get suggestions from server then set suggestions value
        val plants = ArrayList<Plant>()
        plants.add(Plant("Sansa"))

        addPlantViewState.value = AddPlantViewState.SuggestionsFound(plants)
    }
}