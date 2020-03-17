package com.spacebitlabs.plantly.reminder

import com.google.common.truth.Truth
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset
import kotlin.time.ExperimentalTime
import kotlin.time.hours

@OptIn(ExperimentalTime::class)
class WorkReminderTest {

    @Before
    fun setUp() {
    }

    @Test
    fun getInitialDelay_nowIs9am_returnDelayUntil11() {
        val workReminder = WorkReminder(mockk(), TestPrefs())
        val before11 = OffsetDateTime.of(2019, 7, 24, 9, 0, 0, 0, ZoneOffset.ofHours(-5))

        val twoHourMillis = 2.hours.inMilliseconds.toLong()

        val initialDelay = workReminder.getInitialDelay(before11)
        Truth.assertThat(initialDelay).isEqualTo(twoHourMillis)
    }

    @Test
    fun getInitialDelay_nowIsAfter11_returnDelayUntilNext11() {
        val workReminder = WorkReminder(mockk(), TestPrefs())
        val after11 = OffsetDateTime.of(2019, 7, 24, 12, 0, 0, 0, ZoneOffset.ofHours(-5))

        val twentyThreeHourMillis = 23.hours.inMilliseconds.toLong()

        val initialDelay = workReminder.getInitialDelay(after11)
        Truth.assertThat(initialDelay).isEqualTo(twentyThreeHourMillis)
    }

    @Test
    fun getInitialDelay_nowIs11_returnDelay0() {
        val workReminder = WorkReminder(mockk(), TestPrefs())
        val after11 = OffsetDateTime.of(2019, 7, 24, 11, 0, 0, 0, ZoneOffset.ofHours(-5))

        val zeroMillis = 0

        val initialDelay = workReminder.getInitialDelay(after11)
        Truth.assertThat(initialDelay).isEqualTo(zeroMillis)
    }
}