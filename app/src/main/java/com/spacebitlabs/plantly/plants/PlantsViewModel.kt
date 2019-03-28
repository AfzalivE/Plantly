package com.spacebitlabs.plantly.plants

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.spacebitlabs.plantly.Injection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel for Plants list
 */
class PlantsViewModel : ViewModel() {

    /**
     * This is the job for all coroutines started by this ViewModel.
     *
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = Job()

    /**
     * This is the scope for all coroutines launched by [PlantsViewModel].
     *
     * Since we pass [viewModelJob], you can cancel all coroutines launched by [viewModelScope] by calling
     * viewModelJob.cancel().  This is called in [onCleared].
     */
    private val viewModelScope = CoroutineScope(Main + viewModelJob)

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
        viewModelJob.cancel()
    }
}