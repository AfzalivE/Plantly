package com.spacebitlabs.plantly

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.spacebitlabs.plantly.data.EntryType
import com.spacebitlabs.plantly.data.PlantDatabase
import com.spacebitlabs.plantly.data.entities.Entry
import com.spacebitlabs.plantly.data.entities.Plant
import com.spacebitlabs.plantly.data.UserPlantsStore
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.threeten.bp.OffsetDateTime

/**
 * Created by afzal_najam on 2018-03-05.
 */
@RunWith(AndroidJUnit4::class)
class PlantStoreTest {
    private lateinit var db: PlantDatabase
    private lateinit var store: UserPlantsStore

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(context, PlantDatabase::class.java).build()
        store = UserPlantsStore(db, MockWorkReminder())
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
            store.addPlant(
                Plant(
                    id = 1,
                    name = "Test",
                    type = "Test",
                    waterFreq = 1,
                    soilFreq = 1
                )
            )

            val plant = store.getAllPlants()[0]
            val entries = store.getEntries(plant.id)

            val expectedEntry = Entry(id = 1, type = EntryType.BIRTH, time = OffsetDateTime.now(), plantId = 1)

            Assert.assertThat(entries[0], Matchers.equalTo(expectedEntry))
        }
    }
}