package com.spacebitlabs.plantly

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

/**
 * Created by afzal on 2017-11-30.
 */
class MainViewModel : ViewModel() {

    private val plants: MutableLiveData<String> = MutableLiveData()

    fun getPlants() : LiveData<String> = plants
    fun setPlants(plant: String) {
        plants.value = plant
    }
}