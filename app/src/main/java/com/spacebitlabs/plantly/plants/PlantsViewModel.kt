package com.spacebitlabs.plantly.plants

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacebitlabs.plantly.Injection
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel for Plants list
 */
class PlantsViewModel : ViewModel() {

    private val userPlantsStore by lazy {
        Injection.get().providePlantStore()
    }

    internal val plantListState: MutableLiveData<PlantListViewState> = MutableLiveData()

    fun loadUserPlants() {
        Timber.d("Loading users' plants")
        plantListState.value = PlantListViewState.Loading

        viewModelScope.launch {
            val plants = userPlantsStore.getAllPlants()
            Timber.d("Got ${plants.size} plants")

            val todaysPlants = userPlantsStore.getPlantsToWaterToday()

            if (plants.isEmpty()) {
                plantListState.value = PlantListViewState.Empty
            } else {
                plantListState.value = PlantListViewState.PlantsFound(plants, todaysPlants)
            }
        }
    }

    fun deletePlants(plantIdList: List<Long>) {
        viewModelScope.launch {
            userPlantsStore.deletePlants(plantIdList)
            loadUserPlants()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}