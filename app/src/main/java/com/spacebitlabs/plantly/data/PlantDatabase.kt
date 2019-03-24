package com.spacebitlabs.plantly.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.spacebitlabs.plantly.data.dao.EntryDao
import com.spacebitlabs.plantly.data.dao.PlantDao
import com.spacebitlabs.plantly.data.dao.PlantWithPhotosDao
import com.spacebitlabs.plantly.data.entities.Entry
import com.spacebitlabs.plantly.data.entities.Photo
import com.spacebitlabs.plantly.data.entities.Plant

/**
 * Created by afzal_najam on 2018-02-20.
 */
@Database(entities = [Plant::class, Entry::class, Photo::class], version = 4, exportSchema = true)
@TypeConverters(EntryTypeConverters::class)
abstract class PlantDatabase : RoomDatabase() {
    abstract fun plantDao(): PlantDao
    abstract fun entryDao(): EntryDao
    abstract fun plantWithPhotosDao(): PlantWithPhotosDao
}