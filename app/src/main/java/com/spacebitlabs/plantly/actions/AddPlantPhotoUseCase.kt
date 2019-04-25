package com.spacebitlabs.plantly.actions

import com.spacebitlabs.plantly.Injection
import com.spacebitlabs.plantly.data.entities.Photo
import com.spacebitlabs.plantly.data.entities.SimplePhoto

class AddPlantPhotoUseCase {

    private val userPlantsStore by lazy {
        Injection.get().providePlantStore()
    }

    suspend fun addPhoto(plantId: Long, filePath: String) {
        val photo = Photo(plantId, SimplePhoto(filePath))
        userPlantsStore.addPhoto(photo)
    }


}