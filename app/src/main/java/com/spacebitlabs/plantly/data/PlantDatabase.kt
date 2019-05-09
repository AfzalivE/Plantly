package com.spacebitlabs.plantly.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.spacebitlabs.plantly.data.dao.EntryDao
import com.spacebitlabs.plantly.data.dao.PhotoDao
import com.spacebitlabs.plantly.data.dao.PlantDao
import com.spacebitlabs.plantly.data.dao.PlantWithPhotosDao
import com.spacebitlabs.plantly.data.entities.Entry
import com.spacebitlabs.plantly.data.entities.Photo
import com.spacebitlabs.plantly.data.entities.Plant
import org.threeten.bp.OffsetDateTime

/**
 * Created by afzal_najam on 2018-02-20.
 */
@Database(entities = [Plant::class, Entry::class, Photo::class], version = 5, exportSchema = true)
@TypeConverters(EntityTypeConverters::class)
abstract class PlantDatabase : RoomDatabase() {
    abstract fun plantDao(): PlantDao
    abstract fun entryDao(): EntryDao
    abstract fun photoDao(): PhotoDao
    abstract fun plantWithPhotosDao(): PlantWithPhotosDao

    companion object {
        val MIGRATION_4_5 = Migration4To5(4, 5)
    }

    class Migration4To5(startVersion: Int, endVersion: Int): Migration(startVersion, endVersion) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // for photos that don't have a timestamp, use time of migration
            val nowTime = EntityTypeConverters.fromOffsetDateTime(OffsetDateTime.now())
            database.execSQL("ALTER TABLE photo " +
                    " ADD COLUMN time TEXT NOT NULL DEFAULT '$nowTime'")
        }

    }
}