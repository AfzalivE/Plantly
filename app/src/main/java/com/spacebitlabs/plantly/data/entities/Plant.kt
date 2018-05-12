package com.spacebitlabs.plantly.data.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


/**
 * Data class to hold plant info
 */
@Entity
data class Plant(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String
)