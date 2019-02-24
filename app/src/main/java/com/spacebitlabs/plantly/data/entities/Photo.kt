package com.spacebitlabs.plantly.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index("plantId")])
data class Photo(
    val plantId: Long,
    @Embedded
    val simplePhoto: SimplePhoto,
    @PrimaryKey(autoGenerate = true)
    var photoId: Long = 0
)