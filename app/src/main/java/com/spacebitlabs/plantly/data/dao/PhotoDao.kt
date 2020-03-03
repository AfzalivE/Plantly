package com.spacebitlabs.plantly.data.dao

import androidx.room.*
import com.spacebitlabs.plantly.data.entities.Photo

@Dao
interface PhotoDao {

    @Query("SELECT * FROM photo WHERE photoId LIKE :photoId LIMIT 1")
    fun getById(photoId: Long): Photo

    @Query("SELECT * FROM photo where plantId LIKE :plantId")
    fun getPhotosOfPlant(plantId: Long): List<Photo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(photo: Photo): Long

    @Delete
    fun delete(photo: Photo)

    // Delete all entries related to a plant
    @Query("DELETE FROM photo where plantId LIKE :plantId")
    fun deleteAll(plantId: Long)
}