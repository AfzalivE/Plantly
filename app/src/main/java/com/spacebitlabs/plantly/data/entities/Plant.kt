package com.spacebitlabs.plantly.data.entities

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


/**
 * Data class to hold plant info
 */
@Entity
data class Plant(
    val type: String,
    val name: String,
    val waterFreq: Long,
    val soilFreq: Long,
    @Embedded
    var coverPhoto: SimplePhoto = SimplePhoto(""),
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
)