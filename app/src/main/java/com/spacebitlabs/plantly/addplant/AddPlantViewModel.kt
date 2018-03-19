package com.spacebitlabs.plantly.addplant

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.spacebitlabs.plantly.Injection
import com.spacebitlabs.plantly.data.entities.Plant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * ViewModel for Add Plant screen
 */
class AddPlantViewModel : ViewModel() {

    private val disposable = CompositeDisposable()

    private val userPlantsStore by lazy {
        Injection.get().providePlantStore()
    }

    internal val addPlantViewState: MutableLiveData<AddPlantViewState> = MutableLiveData()

    fun requestSuggestions(name: String) {
        addPlantViewState.value = AddPlantViewState.Loading()

        // TODO get suggestions from server then set suggestions value
        val plants = ArrayList<Plant>()
        plants.add(Plant(10, "Sansa"))

        addPlantViewState.value = AddPlantViewState.SuggestionsFound(plants)
    }

    fun addPlant(plant: Plant) {
        disposable.add(
            userPlantsStore.addPlant(plant)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    addPlantViewState.value = AddPlantViewState.Saved()
                }))
    }

    override fun onCleared() {
        disposable.clear()
    }
}