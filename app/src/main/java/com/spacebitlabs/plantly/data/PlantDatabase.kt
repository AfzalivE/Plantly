package com.spacebitlabs.plantly.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.spacebitlabs.plantly.data.entities.Entry
import com.spacebitlabs.plantly.data.entities.Plant
import com.spacebitlabs.plantly.data.source.EntryDao
import com.spacebitlabs.plantly.data.source.PlantDao

/**
 * Created by afzal_najam on 2018-02-20.
 */
@Database(entities = [Plant::class, Entry::class], version = 1)
@TypeConverters(EntryTypeConverters::class)
abstract class PlantDatabase : RoomDatabase() {
    abstract fun plantDao(): PlantDao
    abstract fun entryDao(): EntryDao
}