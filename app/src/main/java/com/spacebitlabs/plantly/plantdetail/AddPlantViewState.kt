package com.spacebitlabs.plantly.plantdetail

import com.spacebitlabs.plantly.data.entities.Plant

/**
 * States for the AddPlant screen
 */
sealed class AddPlantViewState {

//    class Loading : AddPlantViewState()
//    class Empty : AddPlantViewState()
//    class SuggestionsFound(val plantList: List<Plant>) : AddPlantViewState()
    class PlantSelected(val plant: Plant) : AddPlantViewState()
}