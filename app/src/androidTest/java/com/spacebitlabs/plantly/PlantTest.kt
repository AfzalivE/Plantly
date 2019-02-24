package com.spacebitlabs.plantly

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.spacebitlabs.plantly.data.PlantDatabase
import com.spacebitlabs.plantly.data.entities.Plant
import com.spacebitlabs.plantly.data.source.PlantDao
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by afzal_najam on 2018-03-05.
 */
@RunWith(AndroidJUnit4::class)
class PlantTest {
    private lateinit var db: PlantDatabase
    private lateinit var plantDao: PlantDao

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getTargetContext()
        db = Room.inMemoryDatabaseBuilder(context, PlantDatabase::class.java).build()
        plantDao = db.plantDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun createPlant() {
        val plant = Plant(
            id = 1,
            name = "Test",
            type = "Test",
            waterFreq = 1,
            soilFreq = 1
        )

        plantDao.insert(plant)

        val plants = plantDao.getAll()
        assertThat(plants[0].name, equalTo(plant.name))
    }
}