package com.spacebitlabs.plantly.data.source

import com.spacebitlabs.plantly.data.EntryType
import com.spacebitlabs.plantly.data.PlantDatabase
import com.spacebitlabs.plantly.data.entities.Entry
import com.spacebitlabs.plantly.data.entities.Plant

/**
 * Store for the user's plants
 */
class UserPlantsStore(private val database: PlantDatabase) {
    val plants: ArrayList<Plant> = ArrayList()

    init {
//        loadMockData()
    }

//    private fun loadMockData() {
//        database.plantDao().deleteAll()
//
//        val mockPlants = listOf(
//            Plant(0, "Sansa"),
//            Plant(1, "Drogo"),
//            Plant(2, "Cro"),
//            Plant(3, "Krypton"),
//            Plant(4, "Xenon"),
//            Plant(5, "Argon"),
//            Plant(6, "Sansa"),
//            Plant(7, "Drogo"),
//            Plant(8, "Cro"),
//            Plant(9, "Krypton"),
//            Plant(10, "Xenon"),
//            Plant(11, "Argon")
//        )
//        database.plantDao().insertAll(mockPlants)
//    }

    fun getAllPlants(): List<Plant> {
        return database.plantDao().getAll()
    }

    /**
     * Used for creating a new plant
     */
    fun addPlant(plant: Plant) {
        val plantId = database.plantDao().insert(plant)
        database.entryDao().insert(Entry(type = EntryType.BIRTH, plantId = plantId))
    }

    /**
     * Used for changing core plant properties
     * like name
     */
    fun updatePlant(plant: Plant) {
        // TODO if name changed, insert a christening event
        // TODO if photo added/changed, insert a photo event
        database.plantDao().update(plant)
    }

    fun getPlant(id: Long): Plant {
        return database.plantDao().getById(id)
    }

    fun addEntry(event: Entry) {
        database.entryDao().insert(event)
    }

    fun getEntries(plant: Plant): List<Entry> {
        return database.entryDao().getEvents(plantId = plant.id)
    }
}