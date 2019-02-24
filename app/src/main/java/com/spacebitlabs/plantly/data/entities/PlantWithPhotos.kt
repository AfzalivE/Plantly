package com.spacebitlabs.plantly.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class PlantWithPhotos @JvmOverloads constructor(
    @Embedded
    var plant: Plant,

    @Relation(
        parentColumn = "id",
        entityColumn = "plantId"
    )
    var photos: List<Photo> = ArrayList()
)