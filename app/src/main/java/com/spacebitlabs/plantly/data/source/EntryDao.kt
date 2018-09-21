package com.spacebitlabs.plantly.data.source

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.spacebitlabs.plantly.data.EntryType
import com.spacebitlabs.plantly.data.entities.Entry

@Dao
interface EntryDao {
    @Query("SELECT * FROM entry WHERE plantId LIKE :plantId ORDER BY datetime(time)")
    fun getEvents(plantId: Long): List<Entry>

    @Query("SELECT * FROM entry WHERE plantId LIKE :plantId AND type LIKE :type ORDER BY datetime(time)")
    fun getEventsByType(plantId: Long, type: EntryType): List<Entry>

    @Insert(onConflict = REPLACE)
    fun insert(entry: Entry)

    @Update(onConflict = REPLACE)
    fun update(entry: Entry)

    @Delete
    fun delete(entry: Entry)

    // Not sure where this would be useful
    @Query("DELETE FROM entry where plantId LIKE :plantId")
    fun deleteAll(plantId: Long)

    // TODO maybe in the future, we can have queries for counts too
}