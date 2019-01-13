package com.spacebitlabs.plantly.plants

import com.spacebitlabs.plantly.data.entities.Plant

/**
 * ViewStates for PlantList screen
 */
sealed class PlantListViewState {

    class PlantsFound(val plants: List<Plant>, val todayPlants: List<Plant>) : PlantListViewState()
    object Empty : PlantListViewState()
    object Loading : PlantListViewState()
    object Error : PlantListViewState()
}