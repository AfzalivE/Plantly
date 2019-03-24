package com.spacebitlabs.plantly

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.spacebitlabs.plantly.data.EntryType
import com.spacebitlabs.plantly.data.PlantDatabase
import com.spacebitlabs.plantly.data.entities.Entry
import com.spacebitlabs.plantly.data.entities.Plant
import com.spacebitlabs.plantly.data.dao.EntryDao
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
class EntryTest {
    private lateinit var db: PlantDatabase
    private lateinit var entryDao: EntryDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, PlantDatabase::class.java).build()
        entryDao = db.entryDao()

        db.plantDao().insert(
            Plant(
                id = 1,
                name = "Test",
                type = "Test",
                waterFreq = 1,
                soilFreq = 1
            )
        )
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun createEntry() {
        val time = OffsetDateTime.now()
        val entry = Entry(type = EntryType.BIRTH, time = time, plantId = 1)
        entryDao.insert(entry)

        val plants = entryDao.getEvents(1)
        val expectedEntry = Entry(id = 1, type = EntryType.BIRTH, time = time, plantId = 1)

        Assert.assertThat(plants[0], Matchers.equalTo(expectedEntry))
    }
}