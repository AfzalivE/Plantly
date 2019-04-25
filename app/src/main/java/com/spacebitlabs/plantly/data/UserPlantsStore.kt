package com.spacebitlabs.plantly.data

import com.spacebitlabs.plantly.data.entities.Entry
import com.spacebitlabs.plantly.data.entities.Photo
import com.spacebitlabs.plantly.data.entities.Plant
import com.spacebitlabs.plantly.data.entities.PlantWithPhotos
import com.spacebitlabs.plantly.millisFreqToDays
import com.spacebitlabs.plantly.reminder.WorkReminder
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.temporal.ChronoField

/**
 * Store for the user's plants
 */
class UserPlantsStore(private val database: PlantDatabase, private val workReminder: WorkReminder) {

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
            database.photoDao().deleteAll(plant.id)

            if (database.plantDao().count() == 0) {
                workReminder.cancelDailyReminder()
            }
        }
    }

    suspend fun deletePlants(plantIdList: List<Long>) {
        return withContext(IO) {
            database.plantDao().deleteWithIds(plantIdList)
            database.entryDao().deleteWithPlantId(plantIdList)

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

    suspend fun getPlantsToWaterToday(): List<Plant> {
        return getAllPlants().map {
            // TODO optimize getting the last watering entry, don't get all of them
            val lastWaterEntry = getLastEntryOfType(it, EntryType.WATER)
            if (lastWaterEntry != null) {
                Pair(it, lastWaterEntry)
            } else {
                // if this plant has never been watered, compare with birth entry.
                // if birth entry doesn't exist, we have a problem
                val birthEntry = getLastEntryOfType(it, EntryType.BIRTH)
                Pair(it, birthEntry!!)
            }
        }.filter {
            val (plant, entry) = it

            val waterFreqDays = plant.waterFreq.millisFreqToDays().toLong()
            val lastWaterDate = entry.time
            val endOfDay = OffsetDateTime.now().apply {
                with(ChronoField.HOUR_OF_DAY, 23)
                with(ChronoField.MINUTE_OF_HOUR, 59)
                with(ChronoField.SECOND_OF_MINUTE, 59)
            }

            return@filter lastWaterDate.plusDays(waterFreqDays).isBefore(endOfDay)
        }.map {
            return@map it.first
        }
            .toList()
    }

    suspend fun addEntry(event: Entry) {
        return withContext(IO) {
            database.entryDao().insert(event)
        }
    }

    suspend fun getEntries(plantId: Long): List<Entry> {
        return withContext(IO) {
            database.entryDao().getEvents(plantId)
        }
    }

    suspend fun getEntriesOfType(plant: Plant, type: EntryType): List<Entry> {
        return withContext(IO) {
            database.entryDao().getEventsOfType(plant.id, type)
        }
    }

    suspend fun getLastEntryOfType(plant: Plant, type: EntryType): Entry? {
        return withContext(IO) {
            database.entryDao().getLastEventOfType(plant.id, type)
        }
    }

    suspend fun addPhoto(photo: Photo): Long {
        return withContext(IO) {
            database.photoDao().insert(photo)
        }
    }

    suspend fun deletePhoto(photo: Photo) {
        withContext(IO) {
            database.photoDao().delete(photo)
        }
    }

    fun loadMockSeedData() {
        GlobalScope.launch {
            withContext(IO) {
                if (database.plantDao().count() == 0) {
                    database.plantDao().deleteAll()
                    for (plant in mockPlants) {
                        addPlant(plant)
                    }
                }

                database.plantDao().getAll().take(3).forEach {
                    if (database.entryDao().count() == 0) {
                        database.entryDao().insert(
                            Entry(
                                type = EntryType.WATER,
                                plantId = it.id,
                                time = OffsetDateTime.now()
                            )
                        )
                    }
                }
            }
        }
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