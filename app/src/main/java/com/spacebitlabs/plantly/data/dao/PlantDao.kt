package com.spacebitlabs.plantly.data.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.spacebitlabs.plantly.data.entities.Plant

@Dao
interface PlantDao {

    @Query("SELECT * FROM plant")
    fun getAll(): List<Plant>

    @Query("SELECT * FROM plant WHERE id LIKE :id LIMIT 1")
    fun getById(id: Long): Plant

    @Insert(onConflict = REPLACE)
    fun insert(plant: Plant): Long

    // Not sure where this would be useful
    @Insert
    fun insertAll(plants: List<Plant>)

    @Update(onConflict = REPLACE)
    fun update(plant: Plant)

    @Delete
    fun delete(plant: Plant)

    @Query("DELETE FROM plant WHERE id in (:plantIdList)")
    fun deleteWithIds(plantIdList: List<Long>)

    @Query("DELETE FROM plant")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM plant")
    fun count(): Int
}