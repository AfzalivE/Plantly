package com.spacebitlabs.plantly.plantdetail

import com.spacebitlabs.plantly.data.entities.PlantWithPhotos

sealed class PlantDetailViewState {
    class PlantDetailLoaded(val plant: PlantWithPhotos) : PlantDetailViewState()
}