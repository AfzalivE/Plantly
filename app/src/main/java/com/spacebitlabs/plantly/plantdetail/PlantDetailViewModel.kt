package com.spacebitlabs.plantly.plantdetail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.spacebitlabs.plantly.Injection
import com.spacebitlabs.plantly.data.EntryType
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * ViewModel for Add Plant screen
 */
class PlantDetailViewModel : ViewModel() {

    private val disposable = CompositeDisposable()

    private val userPlantsStore by lazy {
        Injection.get().providePlantStore()
    }

    val plantDetailViewState: MutableLiveData<PlantDetailViewState> = MutableLiveData()

    // TODO take this out to a use case class
    fun getPlantDetail(plantId: Long) {
        disposable.add(userPlantsStore.getPlantWithPhotos(plantId)
            .map { plantWithPhotos ->
                val entries = userPlantsStore.getEntries(plantWithPhotos.plant)
                val birthday = entries.filter {
                    it.type == EntryType.BIRTH
                }
                val waterCount = entries.filter {
                    it.type == EntryType.WATER
                }.size
                val soilCount = entries.filter {
                    it.type == EntryType.SOIL
                }.size

                PlantDetailViewState.PlantDetailLoaded(plantWithPhotos, birthday[0].time, waterCount, soilCount)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                plantDetailViewState.value = it
            })
    }
}