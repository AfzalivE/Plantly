package com.spacebitlabs.plantly.plantdetail

import com.spacebitlabs.plantly.data.entities.PlantWithPhotos
import org.threeten.bp.OffsetDateTime

sealed class PlantDetailViewState {
    class PlantDetailLoaded(
        val plant: PlantWithPhotos,
        val birthday: OffsetDateTime,
        val waterCount: Int,
        val soilCount: Int
    ) : PlantDetailViewState()
}