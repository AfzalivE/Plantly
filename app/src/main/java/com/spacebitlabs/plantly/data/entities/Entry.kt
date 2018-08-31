package com.spacebitlabs.plantly.data.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.spacebitlabs.plantly.data.EntryType
import org.threeten.bp.OffsetDateTime

/**
 * Data class to hold each entry related to a plant
 */
@Entity
data class Entry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: EntryType,
    val time: OffsetDateTime = OffsetDateTime.now(),
    val plantId: Long
)