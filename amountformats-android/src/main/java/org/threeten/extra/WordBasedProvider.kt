package org.threeten.extra

import android.content.Context
import java.io.IOException
import java.io.InputStream
import java.util.*

class WordBasedProvider(
    val context: Context,
    private val assetFolderPath: String
) {

    init {
        AmountFormats.registerProvider(this)
    }

    fun getLocale(locale: Locale): LocaleWordBasedProvider? {
        lateinit var provider: LocaleWordBasedProvider
        var inputStream: InputStream? = null
        val assetPath = "$assetFolderPath${getLocaleFileName(locale)}"
        try {
            inputStream = context.assets.open(assetPath)
            provider = LocaleWordBasedProvider(inputStream)
        } catch (ex: IOException) {
            throw IllegalStateException("$assetPath is missing from assets", ex)
        } finally {
            try {
                inputStream?.close()
            } catch (ignored: IOException) {
            }
        }

        return provider
    }

    private fun getLocaleFileName(locale: Locale): String {
        return "wordbased${localeFileNameMap[locale.language]}"
    }

    companion object {

        private val localeFileNameMap = mapOf(
            "en" to ".properties",
            "bg" to "_bg.properties",
            "ca" to "_ca.properties",
            "cs" to "_cs.properties",
            "da" to "_da.properties",
            "de" to "_de.properties",
            "en" to "_en.properties",
            "es" to "_es.properties",
            "fa" to "_fa.properties",
            "fi" to "_fi.properties",
            "fr" to "_fr.properties",
            "it" to "_it.properties",
            "ja" to "_ja.properties",
            "nb" to "_nb.properties",
            "nl" to "_nl.properties",
            "nn" to "_nn.properties",
            "pl" to "_pl.properties",
            "pt" to "_pt.properties",
            "ro" to "_ro.properties",
            "ru" to "_ru.properties",
            "sv" to "_sv.properties",
            "tr" to "_tr.properties"
        )

        /**
         * The property key for the separator ", ".
         */
        internal const val WORDBASED_COMMASPACE = "WordBased.commaspace"
        /**
         * The property key for the separator " and ".
         */
        internal const val WORDBASED_SPACEANDSPACE = "WordBased.spaceandspace"
        /**
         * The property key for the word "year".
         */
        internal const val WORDBASED_YEAR = "WordBased.year"
        /**
         * The property key for the word "month".
         */
        internal const val WORDBASED_MONTH = "WordBased.month"
        /**
         * The property key for the word "week".
         */
        internal const val WORDBASED_WEEK = "WordBased.week"
        /**
         * The property key for the word "day".
         */
        internal const val WORDBASED_DAY = "WordBased.day"
        /**
         * The property key for the word "hour".
         */
        internal const val WORDBASED_HOUR = "WordBased.hour"
        /**
         * The property key for the word "minute".
         */
        internal const val WORDBASED_MINUTE = "WordBased.minute"
        /**
         * The property key for the word "second".
         */
        internal const val WORDBASED_SECOND = "WordBased.second"
        /**
         * The property key for the word "millisecond".
         */
        internal const val WORDBASED_MILLISECOND = "WordBased.millisecond"
    }
}

