package com.spacebitlabs.plantly.data.entities

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

data class PlantWithPhotos @JvmOverloads constructor(
    @Embedded
    var plant: Plant,

    @Relation(
        parentColumn = "id",
        entityColumn = "plantId"
    )
    var photos: List<Photo> = ArrayList()
)