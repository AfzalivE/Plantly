package com.spacebitlabs.plantly

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.spacebitlabs.plantly.models.Plant

/**
 * Created by afzal on 2017-11-30.
 */
class MainViewModel : ViewModel() {

    private val plants: MutableLiveData<Plant> = MutableLiveData()

    fun getPlants() = plants
    fun setPlants(plant: Plant) {
        plants.value = plant
    }

    fun addPlantClicked() {

    }
}