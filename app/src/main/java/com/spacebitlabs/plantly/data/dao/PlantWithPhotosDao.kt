package com.spacebitlabs.plantly.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.spacebitlabs.plantly.data.entities.PlantWithPhotos

@Dao
interface PlantWithPhotosDao {

    @Query("SELECT * from plant")
    @Transaction
    fun getPlantWithPhotos(): List<PlantWithPhotos>

    @Query("SELECT * FROM plant WHERE id LIKE :id LIMIT 1")
    @Transaction
    fun getById(id: Long): PlantWithPhotos

}