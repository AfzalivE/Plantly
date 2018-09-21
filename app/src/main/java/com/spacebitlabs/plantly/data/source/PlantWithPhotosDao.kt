package com.spacebitlabs.plantly.data.source

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import com.spacebitlabs.plantly.data.entities.PlantWithPhotos
import io.reactivex.Flowable

@Dao
interface PlantWithPhotosDao {

    @Query("SELECT * from plant")
    @Transaction
    fun getPlantWithPhotos(): List<PlantWithPhotos>

    @Query("SELECT * FROM plant WHERE id LIKE :id LIMIT 1")
    @Transaction
    fun getById(id: Long): Flowable<PlantWithPhotos>

}