package com.spacebitlabs.plantly.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


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