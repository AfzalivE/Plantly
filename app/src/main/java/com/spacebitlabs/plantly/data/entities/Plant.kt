package com.spacebitlabs.plantly.data.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.kizitonwose.time.Day
import com.kizitonwose.time.Interval
import com.kizitonwose.time.days
import com.kizitonwose.time.milliseconds


/**
 * Data class to hold plant info
 */
@Entity
data class Plant(
    val type: String,
    val name: String,
    val waterFreq: Long,
    val soilFreq: Long,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
) {
    companion object {
        private const val DAYS_IN_WEEK = 7

        fun wordsFreqInMillis(freq: String, type: String): Long {
            val freqInt = freq.toInt()
            return when (type.toLowerCase()) {
                "week", "weeks" -> (freqInt * DAYS_IN_WEEK)
                else            -> freqInt // day case
            }.days.inMilliseconds.longValue
        }

        fun millisFreqToWords(millis: Long): Interval<Day> {
            return millis.milliseconds.inDays
        }
    }
}