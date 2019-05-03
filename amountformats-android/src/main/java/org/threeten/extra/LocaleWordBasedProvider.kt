package org.threeten.extra

import java.io.IOException
import java.io.InputStream
import java.util.*

class LocaleWordBasedProvider(inputStream: InputStream) {
    private val properties = Properties()
    init {
        try {
            properties.load(inputStream)
        } catch (ex: IOException) {
            IllegalStateException("Unable to load inputstream", ex)
        }
    }

    fun getString(key: String): String {
        return properties[key] as String
    }

    fun containsKey(key: String): Boolean {
        return properties.contains(key)
    }
}