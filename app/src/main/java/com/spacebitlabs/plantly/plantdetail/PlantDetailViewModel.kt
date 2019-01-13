package com.spacebitlabs.plantly.plantdetail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.spacebitlabs.plantly.Injection
import com.spacebitlabs.plantly.actions.WaterPlantUseCase
import com.spacebitlabs.plantly.data.EntryType
import com.spacebitlabs.plantly.plants.PlantsViewModel
import kotlinx.coroutines.*
import org.threeten.bp.OffsetDateTime
import timber.log.Timber

/**
 * ViewModel for Add Plant screen
 */
class PlantDetailViewModel : ViewModel() {

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

    private var plantId = 0L

    private val userPlantsStore by lazy {
        Injection.get().providePlantStore()
    }

    private val waterPlantUseCase = WaterPlantUseCase()

    val plantDetailViewState: MutableLiveData<PlantDetailViewState> = MutableLiveData()

    fun waterPlant() {
        waterPlantUseCase.waterPlant(plantId)
    }

    // TODO take this out to a use case class
    fun getPlantDetail(plantId: Long) {
        this.plantId = plantId

        viewModelScope.launch {
            val plant = userPlantsStore.getPlantWithPhotos(plantId)
            val entries = userPlantsStore.getEntries(plantId)

            val birthday = entries.filter {
                it.type == EntryType.BIRTH
            }
            val waterCount = entries.filter {
                it.type == EntryType.WATER
            }.size
            val soilCount = entries.filter {
                it.type == EntryType.SOIL
            }.size

            plantDetailViewState.value = PlantDetailViewState.PlantDetailLoaded(
                plant,
                if (birthday.isEmpty()) OffsetDateTime.now() else birthday[0].time,
                waterCount,
                soilCount
            )

            Timber.d("Fetched entries: $entries")
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}