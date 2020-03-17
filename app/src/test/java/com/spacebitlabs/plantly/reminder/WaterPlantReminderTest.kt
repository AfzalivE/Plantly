package com.spacebitlabs.plantly.reminder

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Test
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset

class WaterPlantReminderTest {

    @Test
    fun doWork() {
    }

    @Test
    fun `9am is not wrong time`() {
        val waterPlantReminder = WaterPlantReminder(mockk(), mockk())

        val after11 = OffsetDateTime.of(2019, 7, 24, 9, 0, 0, 0, ZoneOffset.ofHours(-5))
        Truth.assertThat(waterPlantReminder.isWrongTime(after11)).isFalse()
    }

    @Test
    fun `11am is not wrong time`() {
        val waterPlantReminder = WaterPlantReminder(mockk(), mockk())

        val after11 = OffsetDateTime.of(2019, 7, 24, 11, 0, 0, 0, ZoneOffset.ofHours(-5))
        Truth.assertThat(waterPlantReminder.isWrongTime(after11)).isFalse()
    }

    @Test
    fun `12pm is not wrong time`() {
        val waterPlantReminder = WaterPlantReminder(mockk(), mockk())

        val after11 = OffsetDateTime.of(2019, 7, 24, 12, 0, 0, 0, ZoneOffset.ofHours(-5))
        Truth.assertThat(waterPlantReminder.isWrongTime(after11)).isFalse()
    }

    @Test
    fun `8am is wrong time`() {
        val waterPlantReminder = WaterPlantReminder(mockk(), mockk())

        val after11 = OffsetDateTime.of(2019, 7, 24, 8, 0, 0, 0, ZoneOffset.ofHours(-5))
        Truth.assertThat(waterPlantReminder.isWrongTime(after11)).isTrue()
    }

    @Test
    fun `1pm is wrong time`() {
        val waterPlantReminder = WaterPlantReminder(mockk(), mockk())

        val after11 = OffsetDateTime.of(2019, 7, 24, 13, 0, 0, 0, ZoneOffset.ofHours(-5))
        Truth.assertThat(waterPlantReminder.isWrongTime(after11)).isTrue()
    }

    @Test
    fun `11pm is wrong time`() {
        val waterPlantReminder = WaterPlantReminder(mockk(), mockk())

        val after11 = OffsetDateTime.of(2019, 7, 24, 23, 0, 0, 0, ZoneOffset.ofHours(-5))
        Truth.assertThat(waterPlantReminder.isWrongTime(after11)).isTrue()
    }
}