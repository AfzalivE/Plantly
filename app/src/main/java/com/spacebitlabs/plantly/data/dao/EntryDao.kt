package com.spacebitlabs.plantly.data.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.spacebitlabs.plantly.data.EntryType
import com.spacebitlabs.plantly.data.entities.Entry

@Dao
interface EntryDao {
    @Query("SELECT * FROM entry WHERE plantId LIKE :plantId ORDER BY datetime(time)")
    fun getEvents(plantId: Long): List<Entry>

    @Query("SELECT * FROM entry WHERE plantId LIKE :plantId AND type LIKE :type ORDER BY datetime(time)")
    fun getEventsOfType(plantId: Long, type: EntryType): List<Entry>

    @Query("SELECT * FROM entry WHERE plantId LIKE :plantId AND type LIKE :type ORDER BY datetime(time) LIMIT 1")
    fun getLastEventOfType(plantId: Long, type: EntryType): Entry?

    @Insert(onConflict = REPLACE)
    fun insert(entry: Entry)

    @Update(onConflict = REPLACE)
    fun update(entry: Entry)

    @Delete
    fun delete(entry: Entry)

    @Query("DELETE FROM entry where plantId in (:plantIdList)")
    fun deleteWithPlantId(plantIdList: List<Long>)

    // Delete all entries related to a plant
    @Query("DELETE FROM entry where plantId LIKE :plantId")
    fun deleteAll(plantId: Long)

    @Query("SELECT COUNT(*) FROM entry")
    fun count(): Int
}