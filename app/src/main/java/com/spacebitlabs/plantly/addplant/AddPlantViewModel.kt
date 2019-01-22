package com.spacebitlabs.plantly.addplant

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.spacebitlabs.plantly.Injection
import com.spacebitlabs.plantly.data.entities.Plant
import com.spacebitlabs.plantly.data.entities.SimplePhoto
import com.spacebitlabs.plantly.plants.PlantsViewModel
import kotlinx.coroutines.*

/**
 * ViewModel for Add Plant screen
 */
class AddPlantViewModel : ViewModel() {

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
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var plantCoverImagePath = ""

    private val userPlantsStore by lazy {
        Injection.get().providePlantStore()
    }

    internal val addPlantViewState: MutableLiveData<AddPlantViewState> = MutableLiveData()

    fun requestSuggestions(name: String) {
        addPlantViewState.value = AddPlantViewState.Loading()

        // TODO get suggestions from server then set suggestions value
        val plants = ArrayList<Plant>()
        plants.add(Plant("Sansevieria Trifasciata", "Sansa", 3, 7))

        addPlantViewState.value = AddPlantViewState.SuggestionsFound(plants)
    }

    fun savePlant(plant: Plant) {
        plant.coverPhoto = SimplePhoto(plantCoverImagePath)

        viewModelScope.launch {
            userPlantsStore.addPlant(plant)
            addPlantViewState.value = AddPlantViewState.Saved()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    fun addPlantCoverImage(filePath: String) {
        plantCoverImagePath = filePath
    }
}