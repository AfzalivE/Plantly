package com.spacebitlabs.plantly.plantdetail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.spacebitlabs.plantly.Injection
import com.spacebitlabs.plantly.actions.WaterPlantUseCase
import com.spacebitlabs.plantly.data.EntryType
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Flowables
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.OffsetDateTime
import timber.log.Timber

/**
 * ViewModel for Add Plant screen
 */
class PlantDetailViewModel : ViewModel() {

    private val disposable = CompositeDisposable()
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

        disposable.add(Flowables.combineLatest(
            userPlantsStore.getPlantWithPhotos(plantId),
            userPlantsStore.getEntries(plantId)
        ) { plant, entries ->
            Pair(plant, entries)
        }.map { pair ->
            val (plantWithPhotos, entries) = pair
            val birthday = entries.filter {
                it.type == EntryType.BIRTH
            }
            val waterCount = entries.filter {
                it.type == EntryType.WATER
            }.size
            val soilCount = entries.filter {
                it.type == EntryType.SOIL
            }.size

            PlantDetailViewState.PlantDetailLoaded(
                plantWithPhotos,
                if (birthday.isEmpty()) OffsetDateTime.now() else birthday[0].time,
                waterCount,
                soilCount
            )
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                plantDetailViewState.value = it
            })

        userPlantsStore.getEntries(plantId)
            .subscribe {
                Timber.d("Updating entries: $it")
            }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}