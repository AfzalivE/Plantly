package com.spacebitlabs.plantly.data

import androidx.room.TypeConverter
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

object EntryTypeConverters {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @TypeConverter
    @JvmStatic
    fun toOffsetDateTime(value: String?): OffsetDateTime? {
        return value?.let {
            return formatter.parse(value, OffsetDateTime::from)
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromOffsetDateTime(date: OffsetDateTime?): String? = date?.format(formatter)

    @TypeConverter
    @JvmStatic
    fun toEventType(value: Int): EntryType = EntryType.values()[value]

    @TypeConverter
    @JvmStatic
    fun toEventValue(type: EntryType): Int = type.ordinal
}
