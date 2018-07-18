package com.spacebitlabs.plantly.plants

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.spacebitlabs.plantly.Injection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * ViewModel for Plants list
 */
class PlantsViewModel : ViewModel() {

    private val disposable = CompositeDisposable()

    private val userPlantsStore by lazy {
        Injection.get().providePlantStore()
    }

    internal val plantListState: MutableLiveData<PlantListViewState> = MutableLiveData()

    fun loadUserPlants() {
        Timber.d("Loading users' plants")
        plantListState.value = PlantListViewState.Loading()

        disposable.add(
            userPlantsStore.getAllPlants()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { plantListState.value = PlantListViewState.Loading() }
                .subscribe {
                    Timber.d("Got ${it.size} plants")
                    if (it.isEmpty()) {
                        plantListState.value = PlantListViewState.Empty()
                    } else {
                        plantListState.value = PlantListViewState.PlantsFound(it)
                    }
                }
        )
    }

    fun addPlantClicked() {

    }

    override fun onCleared() {
        disposable.clear()
    }
}