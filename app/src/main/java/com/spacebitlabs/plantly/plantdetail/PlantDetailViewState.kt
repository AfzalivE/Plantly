package com.spacebitlabs.plantly.plantdetail

import com.spacebitlabs.plantly.data.entities.Plant

sealed class PlantDetailViewState {
    class PlantDetailLoaded(val plant: Plant) : PlantDetailViewState()
}