package com.spacebitlabs.plantly.data.source

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import com.spacebitlabs.plantly.data.entities.PlantWithPhotos

@Dao
interface PlantWithPhotosDao {

    @Query("SELECT * from plant")
    @Transaction
    fun getPlantWithPhotos(): List<PlantWithPhotos>
}