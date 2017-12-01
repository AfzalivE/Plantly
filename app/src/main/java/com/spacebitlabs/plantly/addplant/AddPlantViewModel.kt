package com.spacebitlabs.plantly.addplant

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.spacebitlabs.plantly.models.Plant

/**
 * Created by afzal on 2017-12-01.
 */
class AddPlantViewModel : ViewModel() {

    var suggestions: MutableLiveData<List<Plant>> = MutableLiveData()
    var plant: MutableLiveData<Plant> = MutableLiveData()

    fun addPlant(plant: Plant) {
        this.plant.value = plant
    }

    fun requestSuggestions(name: String) {
        // TODO get suggestions from server then set suggestions value
        val plantSuggestions = ArrayList<Plant>()
        plantSuggestions.add(Plant("Sansa"))
        suggestions.value = plantSuggestions
    }

    fun getPlantSuggestions(): MutableLiveData<List<Plant>> = suggestions
}