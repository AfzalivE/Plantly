/*
 * Copyright (c) 2007-present, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.threeten.extra

import org.threeten.bp.Duration
import org.threeten.bp.Period
import org.threeten.extra.WordBasedProvider.Companion.WORDBASED_COMMASPACE
import org.threeten.extra.WordBasedProvider.Companion.WORDBASED_DAY
import org.threeten.extra.WordBasedProvider.Companion.WORDBASED_HOUR
import org.threeten.extra.WordBasedProvider.Companion.WORDBASED_MILLISECOND
import org.threeten.extra.WordBasedProvider.Companion.WORDBASED_MINUTE
import org.threeten.extra.WordBasedProvider.Companion.WORDBASED_MONTH
import org.threeten.extra.WordBasedProvider.Companion.WORDBASED_SECOND
import org.threeten.extra.WordBasedProvider.Companion.WORDBASED_SPACEANDSPACE
import org.threeten.extra.WordBasedProvider.Companion.WORDBASED_WEEK
import org.threeten.extra.WordBasedProvider.Companion.WORDBASED_YEAR
import java.util.*
import java.util.regex.Pattern

/**
 * Provides the ability to format a temporal amount.
 *
 *
 * This allows a [TemporalAmount], such as [Duration] or [Period],
 * to be formatted. Only selected formatting options are provided.
 *
 * <h3>Implementation Requirements:</h3>
 * This class is immutable and thread-safe.
 */
object AmountFormats {

    private lateinit var provider: WordBasedProvider
    /**
     * The number of days per week.
     */
    private val DAYS_PER_WEEK = 7
    /**
     * The number of hours per day.
     */
    private val HOURS_PER_DAY = 24
    /**
     * The number of minutes per hour.
     */
    private val MINUTES_PER_HOUR = 60
    /**
     * The number of seconds per minute.
     */
    private val SECONDS_PER_MINUTE = 60
    /**
     * The number of nanosecond per millisecond.
     */
    private val NANOS_PER_MILLIS = 1000000
    /**
     * The resource bundle name.
     */
//    private val BUNDLE_NAME = "org.threeten.extra.wordbased"
    /**
     * The pattern to split lists with.
     */
    private val SPLITTER = Pattern.compile("[|][|][|]")

    /**
     * The predicate that matches 1 or -1.
     */
    private val PREDICATE_1: (Int) -> Boolean = { value -> value == 1 || value == -1 }
    /**
     * The predicate that matches numbers ending 2, 3 or 4, but not ending 12, 13 or 14.
     */
    private val PREDICATE_END234_NOTTEENS: (Int) -> Boolean = { value ->
        val abs = Math.abs(value)
        val last = abs % 10
        val secondLast = abs % 100 / 10
        last >= 2 && last <= 4 && secondLast != 1
    }

    //-----------------------------------------------------------------------
    /**
     * Formats a period and duration to a string in ISO-8601 format.
     *
     *
     * To obtain the ISO-8601 format of a `Period` or `Duration`
     * individually, simply call `toString()`.
     * See also [PeriodDuration].
     *
     * @param period  the period to format
     * @param duration  the duration to format
     * @return the ISO-8601 format for the period and duration
     */
    fun iso8601(period: Period, duration: Duration): String {
        Objects.requireNonNull(period, "period must not be null")
        Objects.requireNonNull(duration, "duration must not be null")
        if (period.isZero) {
            return duration.toString()
        }
        return if (duration.isZero) {
            period.toString()
        } else period.toString() + duration.toString().substring(1)
    }

    //-------------------------------------------------------------------------
    /**
     * Formats a period to a string in a localized word-based format.
     *
     *
     * This returns a word-based format for the period.
     * The year and month are printed as supplied unless the signs differ, in which case they are normalized.
     * The words are configured in a resource bundle text file -
     * `org.threeten.extra.wordbased.properties` - with overrides per language.
     *
     * @param period  the period to format
     * @param locale  the locale to use
     * @return the localized word-based format for the period
     */
    fun wordBased(period: Period, locale: Locale): String {
        Objects.requireNonNull(period, "period must not be null")
        Objects.requireNonNull(locale, "locale must not be null")
//        val bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale)
        val bundle = provider.getLocale(locale)!!
        val formats = arrayOf(UnitFormat.of(bundle, WORDBASED_YEAR), UnitFormat.of(bundle, WORDBASED_MONTH), UnitFormat.of(bundle, WORDBASED_WEEK), UnitFormat.of(bundle, WORDBASED_DAY))
        val wb = WordBased(formats, bundle.getString(WORDBASED_COMMASPACE), bundle.getString(WORDBASED_SPACEANDSPACE))

        val normPeriod = if (oppositeSigns(period.months, period.years)) period.normalized() else period
        var weeks = 0
        var days = 0
        if (normPeriod.days % DAYS_PER_WEEK == 0) {
            weeks = normPeriod.days / DAYS_PER_WEEK
        } else {
            days = normPeriod.days
        }
        val values = intArrayOf(normPeriod.years, normPeriod.months, weeks, days)
        return wb.format(values)
    }

    /**
     * Formats a duration to a string in a localized word-based format.
     *
     *
     * This returns a word-based format for the duration.
     * The words are configured in a resource bundle text file -
     * `org.threeten.extra.wordbased.properties` - with overrides per language.
     *
     * @param duration  the duration to format
     * @param locale  the locale to use
     * @return the localized word-based format for the duration
     */
    fun wordBased(duration: Duration, locale: Locale): String {
        Objects.requireNonNull(duration, "duration must not be null")
        Objects.requireNonNull(locale, "locale must not be null")
//        val bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale)
        val bundle = provider.getLocale(locale)!!
        val formats = arrayOf(UnitFormat.of(bundle, WORDBASED_HOUR), UnitFormat.of(bundle, WORDBASED_MINUTE), UnitFormat.of(bundle, WORDBASED_SECOND), UnitFormat.of(bundle, WORDBASED_MILLISECOND))
        val wb = WordBased(formats, bundle.getString(WORDBASED_COMMASPACE), bundle.getString(WORDBASED_SPACEANDSPACE))

        val hours = duration.toHours()
        val mins = duration.toMinutes() % MINUTES_PER_HOUR
        val secs = duration.seconds % SECONDS_PER_MINUTE
        val millis = duration.nano / NANOS_PER_MILLIS
        val values = intArrayOf(hours.toInt(), mins.toInt(), secs.toInt(), millis)
        return wb.format(values)
    }

    /**
     * Formats a period and duration to a string in a localized word-based format.
     *
     *
     * This returns a word-based format for the period.
     * The year and month are printed as supplied unless the signs differ, in which case they are normalized.
     * The words are configured in a resource bundle text file -
     * `org.threeten.extra.wordbased.properties` - with overrides per language.
     *
     * @param period  the period to format
     * @param duration  the duration to format
     * @param locale  the locale to use
     * @return the localized word-based format for the period and duration
     */
    fun wordBased(period: Period, duration: Duration, locale: Locale): String {
        Objects.requireNonNull(period, "period must not be null")
        Objects.requireNonNull(duration, "duration must not be null")
        Objects.requireNonNull(locale, "locale must not be null")
//        val bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale)
        val bundle = provider.getLocale(locale)!!
        val formats = arrayOf(
            UnitFormat.of(bundle, WORDBASED_YEAR),
            UnitFormat.of(bundle, WORDBASED_MONTH),
            UnitFormat.of(bundle, WORDBASED_WEEK),
            UnitFormat.of(bundle, WORDBASED_DAY),
            UnitFormat.of(bundle, WORDBASED_HOUR),
            UnitFormat.of(bundle, WORDBASED_MINUTE),
            UnitFormat.of(bundle, WORDBASED_SECOND),
            UnitFormat.of(bundle, WORDBASED_MILLISECOND)
        )
        val wb = WordBased(formats, bundle.getString(WORDBASED_COMMASPACE), bundle.getString(WORDBASED_SPACEANDSPACE))

        val normPeriod = if (oppositeSigns(period.months, period.years)) period.normalized() else period
        var weeks = 0
        var days = 0
        if (normPeriod.days % DAYS_PER_WEEK == 0) {
            weeks = normPeriod.days / DAYS_PER_WEEK
        } else {
            days = normPeriod.days
        }
        val totalHours = duration.toHours()
        days += (totalHours / HOURS_PER_DAY).toInt()
        val hours = (totalHours % HOURS_PER_DAY).toInt()
        val mins = (duration.toMinutes() % MINUTES_PER_HOUR).toInt()
        val secs = (duration.seconds % SECONDS_PER_MINUTE).toInt()
        val millis = duration.nano / NANOS_PER_MILLIS
        val values = intArrayOf(normPeriod.years, normPeriod.months, weeks, days, hours, mins, secs, millis)
        return wb.format(values)
    }

    // are the signs opposite
    private fun oppositeSigns(a: Int, b: Int): Boolean {
        return if (a < 0) b >= 0 else b < 0
    }

    //-------------------------------------------------------------------------
    // data holder for word-based formats
    internal class WordBased(private val units: Array<UnitFormat>, private val separator: String, private val lastSeparator: String) {

        fun format(values: IntArray): String {
            val buf = StringBuilder(32)
            var nonZeroCount = 0
            for (i in values.indices) {
                if (values[i] != 0) {
                    nonZeroCount++
                }
            }
            var count = 0
            for (i in values.indices) {
                if (values[i] != 0 || count == 0 && i == values.size - 1) {
                    units[i].formatTo(values[i], buf)
                    if (count < nonZeroCount - 2) {
                        buf.append(separator)
                    } else if (count == nonZeroCount - 2) {
                        buf.append(lastSeparator)
                    }
                    count++
                }
            }
            return buf.toString()
        }
    }

    // data holder for single/plural formats
    internal interface UnitFormat {

        fun formatTo(value: Int, buf: StringBuilder)

        companion object {

            fun of(bundle: LocaleWordBasedProvider, keyStem: String): UnitFormat {
                if (bundle.containsKey(keyStem + "s.predicates")) {
                    val predicateList = bundle.getString(keyStem + "s.predicates")
                    val textList = bundle.getString(keyStem + "s.list")
                    val regexes = SPLITTER.split(predicateList)
                    val text = SPLITTER.split(textList)
                    return PredicateFormat(regexes, text)
                } else {
                    val single = bundle.getString(keyStem)
                    val plural = bundle.getString(keyStem + "s")
                    return SinglePluralFormat(single, plural)
                }
            }
        }
    }

    // data holder for single/plural formats
    internal class SinglePluralFormat(private val single: String, private val plural: String) : UnitFormat {

        override fun formatTo(value: Int, buf: StringBuilder) {
            buf.append(value).append(if (value == 1 || value == -1) single else plural)
        }
    }

    // data holder for predicate formats
    internal class PredicateFormat(predicateStrs: Array<String>, private val text: Array<String>) : UnitFormat {
        private val predicates: Array<(Int) -> Boolean>

        init {
            if (predicateStrs.size + 1 != text.size) {
                throw IllegalStateException("Invalid word-based resource")
            }

            this.predicates = predicateStrs.map { predicateStr ->
                findPredicate(predicateStr)
            }.toTypedArray()
        }

        private fun findPredicate(predicateStr: String): (Int) -> Boolean {
            return when (predicateStr) {
                "One"            -> PREDICATE_1
                "End234NotTeens" -> PREDICATE_END234_NOTTEENS
                else             -> throw IllegalStateException("Invalid word-based resource")
            }
        }

        override fun formatTo(value: Int, buf: StringBuilder) {
            for (i in predicates.indices) {
                if (predicates[i].invoke(value)) {
                    buf.append(value).append(text[i])
                    return
                }
            }
            buf.append(value).append(text[predicates.size])
            return
        }


    }

    fun registerProvider(provider: WordBasedProvider) {
        AmountFormats.provider = provider
    }
}
