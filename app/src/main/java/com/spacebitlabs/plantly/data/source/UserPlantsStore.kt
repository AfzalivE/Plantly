package com.spacebitlabs.plantly.data.source

import com.spacebitlabs.plantly.data.EntryType
import com.spacebitlabs.plantly.data.PlantDatabase
import com.spacebitlabs.plantly.data.entities.Entry
import com.spacebitlabs.plantly.data.entities.Plant
import com.spacebitlabs.plantly.data.entities.PlantWithPhotos
import com.spacebitlabs.plantly.reminder.WorkReminder
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread

/**
 * Store for the user's plants
 */
class UserPlantsStore(private val database: PlantDatabase, private val workReminder: WorkReminder) {
    val plants: ArrayList<Plant> = ArrayList()

    init {

    }

    internal fun loadMockData() {
        thread {
            if (database.plantDao().count() == 0) {
                database.plantDao().deleteAll()
                database.plantDao().insertAll(mockPlants)
            }
        }
    }

    suspend fun getAllPlants(): List<Plant> {
        return withContext(IO) {
            database.plantDao().getAll()
        }
    }

    /**
     * Used for creating a new plant
     */
    suspend fun addPlant(plant: Plant): Long {
        return withContext(IO) {
            val plantId = database.plantDao().insert(plant)
            database.entryDao().insert(Entry(type = EntryType.BIRTH, plantId = plantId))

            if (database.plantDao().count() == 1) {
                workReminder.scheduleDailyReminder()
            }
            return@withContext plantId
        }
    }

    /**
     * Used for creating a new plant
     */
    suspend fun deletePlant(plant: Plant) {
        return withContext(IO) {
            database.plantDao().delete(plant)
            database.entryDao().deleteAll(plant.id)

            if (database.plantDao().count() == 0) {
                workReminder.cancelDailyReminder()
            }
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

    suspend fun getPlant(id: Long): Plant {
        return withContext(IO) {
            database.plantDao().getById(id)
        }
    }

    suspend fun getPlantWithPhotos(id: Long): PlantWithPhotos {
        return withContext(IO) {
            database.plantWithPhotosDao().getById(id)
        }
    }

    fun addEntry(event: Entry) {
        thread {
            database.entryDao().insert(event)
        }
    }

    suspend fun getEntries(plantId: Long): List<Entry> {
        return withContext(IO) {
            database.entryDao().getEvents(plantId)
        }
    }

    fun getEntriesByType(plant: Plant, type: EntryType): List<Entry> {
        return database.entryDao().getEventsByType(plant.id, type)
    }

    companion object {
        private val mockPlants = listOf(
//            Plant("Sansa", "Sansa", 3, 10),
//            Plant("Drogo", "Drogo", 3, 10),
//            Plant("Cro", "Cro", 3, 10),
//            Plant("Krypton", "Krypton", 3, 10),
//            Plant("Xenon", "Xenon", 3, 10),
//            Plant("Argon", "Argon", 3, 10),
//            Plant("Sansa", "Sansa", 3, 10),
            Plant("Drogo", "Drogo", 3, 10),
            Plant("Cro", "Cro", 3, 10),
            Plant("Krypton", "Krypton", 3, 10),
            Plant("Xenon", "Xenon", 3, 10),
            Plant("Argon", "Argon", 3, 10)
        )
    }
}