package com.spacebitlabs.plantly.data.entities

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(indices = [Index("plantId")])
data class Photo(
    val plantId: Long,
    @Embedded
    val simplePhoto: SimplePhoto,
    @PrimaryKey(autoGenerate = true)
    var photoId: Long = 0
)