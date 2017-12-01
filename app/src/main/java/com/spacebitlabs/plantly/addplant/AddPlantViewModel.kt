package com.spacebitlabs.plantly.addplant

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.spacebitlabs.plantly.models.Plant

/**
 * Created by afzal on 2017-12-01.
 */
class AddPlantViewModel : ViewModel() {
    
    var plant: MutableLiveData<Plant> = MutableLiveData()

    fun addPlant(plant: Plant) {
        this.plant.value = plant
    }
}