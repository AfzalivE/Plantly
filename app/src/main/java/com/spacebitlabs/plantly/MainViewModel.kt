package com.spacebitlabs.plantly

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Handler
import com.spacebitlabs.plantly.plants.PlantListViewState
import timber.log.Timber

/**
 * ViewModel for Plants list
 */
class MainViewModel : ViewModel() {

    private val userPlantsStore by lazy {
        Injection.get().providePlantStore()
    }

    internal val plantListState: MutableLiveData<PlantListViewState> = MutableLiveData()

    fun loadUserPlants() {
        Timber.d("Loading users' plants")
        plantListState.value = PlantListViewState.Loading()

        val handler = Handler()
        handler.postDelayed({
            val plants = userPlantsStore.plants
            if (plants.isEmpty()) {
                plantListState.value = PlantListViewState.Empty()
            } else {
                plantListState.value = PlantListViewState.PlantsFound(plants)
            }
        }, 1000)
    }

    fun addPlantClicked() {

    }
}