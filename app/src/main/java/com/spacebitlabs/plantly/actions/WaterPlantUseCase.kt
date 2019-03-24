package com.spacebitlabs.plantly.actions

import com.spacebitlabs.plantly.Injection
import com.spacebitlabs.plantly.data.EntryType
import com.spacebitlabs.plantly.data.entities.Entry

class WaterPlantUseCase {

    private val userPlantsStore by lazy {
        Injection.get().providePlantStore()
    }

    suspend fun waterPlant(plantId: Long) {
        userPlantsStore.addEntry(Entry(type = EntryType.WATER, plantId = plantId))
    }
}