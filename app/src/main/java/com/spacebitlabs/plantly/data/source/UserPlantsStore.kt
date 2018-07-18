package com.spacebitlabs.plantly.data.source

import com.spacebitlabs.plantly.data.EntryType
import com.spacebitlabs.plantly.data.PlantDatabase
import com.spacebitlabs.plantly.data.entities.Entry
import com.spacebitlabs.plantly.data.entities.Plant
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Store for the user's plants
 */
class UserPlantsStore(private val database: PlantDatabase) {
    val plants: ArrayList<Plant> = ArrayList()

    init {
//        loadMockData()
    }

    private fun loadMockData() {
        database.plantDao().deleteAll()

        val mockPlants = listOf(
            Plant("Sansa", "Sansa", 3, 10),
            Plant("Drogo", "Drogo", 3, 10),
            Plant("Cro", "Cro", 3, 10),
            Plant("Krypton", "Krypton", 3, 10),
            Plant("Xenon", "Xenon", 3, 10),
            Plant("Argon", "Argon", 3, 10),
            Plant("Sansa", "Sansa", 3, 10),
            Plant("Drogo", "Drogo", 3, 10),
            Plant("Cro", "Cro", 3, 10),
            Plant("Krypton", "Krypton", 3, 10),
            Plant("Xenon", "Xenon", 3, 10),
            Plant("Argon", "Argon", 3, 10)
        )
        database.plantDao().insertAll(mockPlants)
    }

    fun getAllPlants(): Flowable<List<Plant>> {
        return database.plantDao().getAll()
    }

    /**
     * Used for creating a new plant
     */
    fun addPlant(plant: Plant): Completable {
        return Completable.fromAction {
            loadMockData()
            val plantId = database.plantDao().insert(plant)
            database.entryDao().insert(Entry(type = EntryType.BIRTH, plantId = plantId))
        }
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

    fun getPlant(id: Long): Flowable<Plant> {
        return database.plantDao().getById(id)
    }

    fun addEntry(event: Entry) {
        database.entryDao().insert(event)
    }

    fun getEntries(plant: Plant): List<Entry> {
        return database.entryDao().getEvents(plantId = plant.id)
    }
}