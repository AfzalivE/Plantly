package com.spacebitlabs.plantly.plants

import com.spacebitlabs.plantly.data.models.Plant

/**
 * ViewStates for PlantList screen
 */
sealed class PlantListViewState {

    class PlantsFound(val plants: List<Plant>) : PlantListViewState()

    class Empty : PlantListViewState()

    class Loading : PlantListViewState()

    class Error: PlantListViewState()
}