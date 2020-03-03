package com.spacebitlabs.plantly.plantdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacebitlabs.plantly.Injection
import com.spacebitlabs.plantly.actions.AddPlantPhotoUseCase
import com.spacebitlabs.plantly.actions.WaterPlantUseCase
import com.spacebitlabs.plantly.data.EntryType
import com.spacebitlabs.plantly.millisFreqToDays
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel for Add Plant screen
 */
class PlantDetailViewModel : ViewModel() {

    private var plantId = 0L

    private val userPlantsStore by lazy {
        Injection.get().providePlantStore()
    }

    private val waterPlantUseCase = WaterPlantUseCase()
    private val addPlantPhotoUseCase = AddPlantPhotoUseCase()

    val plantDetailViewState: MutableLiveData<PlantDetailViewState> = MutableLiveData()

    fun waterPlant() {
        viewModelScope.launch {
            waterPlantUseCase.waterPlant(plantId)
            getPlantDetail(plantId)
        }
    }

    fun addPlantPhoto(filePath: String) {
        viewModelScope.launch {
            addPlantPhotoUseCase.addPhoto(plantId, filePath)
            getPlantDetail(plantId)
        }
    }

    // TODO take this out to a use case class
    fun getPlantDetail(plantId: Long) {
        this.plantId = plantId

        viewModelScope.launch {
            val plant = userPlantsStore.getPlantWithPhotos(plantId)
            val entries = userPlantsStore.getEntries(plantId)

            val birthday = entries.first {
                it.type == EntryType.BIRTH
            }

            val waterCount = entries.filter {
                it.type == EntryType.WATER
            }.size
            val soilCount = entries.filter {
                it.type == EntryType.SOIL
            }.size

            val nextWatering = entries.last {
                it.type == EntryType.WATER || it.type == EntryType.BIRTH
            }.time.plusDays(plant.plant.waterFreq.millisFreqToDays().toLong())

            plantDetailViewState.value = PlantDetailViewState.PlantDetailLoaded(
                plant,
                birthday.time,
                waterCount,
                soilCount,
                nextWatering
            )

            Timber.d("Fetched entries: $entries")
        }
    }

    fun deletePlant() {
        viewModelScope.launch {
            userPlantsStore.deletePlant(plantId)
            plantDetailViewState.value = PlantDetailViewState.PlantDeleted
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}