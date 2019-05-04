package com.spacebitlabs.plantly

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth
import com.kizitonwose.time.days
import com.spacebitlabs.plantly.data.EntryType
import com.spacebitlabs.plantly.data.PlantDatabase
import com.spacebitlabs.plantly.data.UserPlantsStore
import com.spacebitlabs.plantly.data.entities.Entry
import com.spacebitlabs.plantly.data.entities.Plant
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.threeten.bp.OffsetDateTime


/**
 * Created by afzal_najam on 2018-03-05.
 */
@RunWith(RobolectricTestRunner::class)
@Config(application = TestPlantlyApp::class)
class PlantStoreUnitTest {
    private lateinit var db: PlantDatabase
    private lateinit var store: UserPlantsStore

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(context, PlantDatabase::class.java).build()
        store = UserPlantsStore(db, EmptyWorkReminder())
    }

    @After
    fun closeDb() {
        db.close()
    }

    /**
     * When a plant is added through the store,
     * a birth entry is created for it.
     *
     * Add a plant, get the newly created plant
     * and check if its birth entry was created
     */
    @Test
    fun createPlant() {
        runBlocking {
            val time = OffsetDateTime.now()

            store.addPlant(
                Plant(
                    id = 1,
                    name = "Test",
                    type = "Test",
                    waterFreq = 1,
                    soilFreq = 1
                ),
                time
            )

            val plant = store.getAllPlants()[0]
            val entries = store.getEntries(plant.id)

            val expectedEntry = Entry(id = 1, type = EntryType.BIRTH, time = time, plantId = 1)

            Truth.assertThat(entries[0]).isEqualTo(expectedEntry)
        }
    }

    @Test
    fun `new plant added, it should not be in plants to water today`() {
        runBlocking {
            val plant = Plant(
                id = 1,
                name = "Test",
                type = "Test",
                waterFreq = 2.days.inMilliseconds.longValue,
                soilFreq = 2.days.inMilliseconds.longValue
            )
            // add a plant to water in 2 days
            store.addPlant(plant)

            Truth.assertThat(store.getPlantsToWaterToday()).isEmpty()
        }
    }

    @Test
    fun `plant to water should be returned on future date`() {
        runBlocking {
            val plant = Plant(
                id = 2,
                name = "Test",
                type = "Test",
                waterFreq = 2.days.inMilliseconds.longValue,
                soilFreq = 2.days.inMilliseconds.longValue
            )
            // add a plant to water in 2 days
            store.addPlant(plant)

            val actualWateringDate = OffsetDateTime.now().plusDays(2)

            Truth.assertThat(store.getPlantsToWaterOn(actualWateringDate).first()).isEqualTo(plant)
        }
    }

    @Test
    fun `plant should not be returned if watered already`() {
        runBlocking {
            val plant = Plant(
                id = 1,
                name = "Test",
                type = "Test",
                waterFreq = 2.days.inMilliseconds.longValue,
                soilFreq = 2.days.inMilliseconds.longValue
            )
            // add a plant to water in 2 days
            store.addPlant(plant)

            val firstWateringDate = OffsetDateTime.now().plusDays(2)
            val secondWateringDate = OffsetDateTime.now().plusDays(4)

            store.addEntry(Entry(type = EntryType.WATER, plantId = 1, time = firstWateringDate))
            store.addEntry(Entry(type = EntryType.WATER, plantId = 1, time = secondWateringDate))

            val beforeNextWateringDate = OffsetDateTime.now().plusDays(5)

            Truth.assertThat(store.getPlantsToWaterOn(beforeNextWateringDate)).isEmpty()
        }
    }
}