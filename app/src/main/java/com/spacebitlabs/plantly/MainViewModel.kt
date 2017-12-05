package com.spacebitlabs.plantly

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.spacebitlabs.plantly.data.source.UserPlantsStore
import com.spacebitlabs.plantly.plants.PlantListViewState

/**
 * ViewModel for Plants list
 */
class MainViewModel : ViewModel() {

    val userPlantsStore: UserPlantsStore = UserPlantsStore()
    internal val plantListState: MutableLiveData<PlantListViewState> = MutableLiveData()

    fun loadUserPlants() {
        plantListState.value = PlantListViewState.Loading()

        val plants = userPlantsStore.plants
        if (plants.isEmpty()) {
            plantListState.value = PlantListViewState.Empty()
        } else {
            plantListState.value = PlantListViewState.PlantsFound(plants)
        }
    }

    fun addPlantClicked() {

    }
}