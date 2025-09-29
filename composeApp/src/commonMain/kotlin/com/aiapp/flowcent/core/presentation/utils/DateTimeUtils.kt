/*
 * Created by Saeedus Salehin on 23/5/25, 10:50 PM.
 */

package com.aiapp.flowcent.core.presentation.utils

import io.github.aakira.napier.Napier
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

object DateTimeUtils {
    fun getCurrentDate(): LocalDate =
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    fun daysInMonth(date: LocalDate): Int {
        val nextMonth = date.plus(1, DateTimeUnit.MONTH)
        return nextMonth.minus(1, DateTimeUnit.DAY).dayOfMonth
    }


    /**
     * Formats the current local time into a string with the format "dd-MM-yyyy HH:mm:ss".
     *
     * @return A string representing the current time in "dd-MM-yyyy HH:mm:ss" format.
     */
    fun getCurrentFormattedDateTime(): String {
        val now = Clock.System.now()
        val localDateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())

        val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
        val month = localDateTime.monthNumber.toString().padStart(2, '0')
        val year = localDateTime.year.toString()
        val hour = localDateTime.hour.toString().padStart(2, '0')
        val minute = localDateTime.minute.toString().padStart(2, '0')
        val second = localDateTime.second.toString().padStart(2, '0')

        return "$day-$month-$year $hour:$minute:$second"
    }

    /**
     * Converts a date string from "YYYY-MM-DD" format to "DD-MM-YYYY" format.
     *
     * @param dateString The input date string from the user, expected in "YYYY-MM-DD" format.
     * @return A formatted string as "DD-MM-YYYY", or null on error.
     */
    fun getFormattedDate(dateString: String): String? {
        return try {
            val localDate = LocalDate.parse(dateString)

            val outputFormat = LocalDate.Format {
                dayOfMonth(padding = Padding.ZERO)
                char('-')
                monthNumber(padding = Padding.ZERO)
                char('-')
                year()
            }

            outputFormat.format(localDate)

        } catch (e: Exception) {
            Napier.e("Error parsing date: ${e.message}")
            null
        }
    }

    fun getCurrentTimeInMilli(): Long {
        return Clock.System.now().toEpochMilliseconds()
    }


    fun getStartAndEndTimeMillis(dateString: String): Pair<Long, Long> {
        val localDate = LocalDate.parse(dateString) // e.g. "2025-08-04"
        val timeZone = TimeZone.currentSystemDefault()

        // Start of day Instant in UTC
        val startInstant = localDate.atStartOfDayIn(timeZone)
        val startMillis = startInstant.toEpochMilliseconds()

        // End of day: 23:59:59.999 (milliseconds precision)
        val endLocalTime = LocalTime(23, 59, 59, 999_000_000) // 999 milliseconds
        val endInstant = localDate.atTime(endLocalTime).toInstant(timeZone)
        val endMillis = endInstant.toEpochMilliseconds()

        return startMillis to endMillis
    }


    fun getCurrentMonthStartAndEndMillis(): Pair<Long, Long> {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val timeZone = TimeZone.currentSystemDefault()

        // Start of the month
        val startDate = LocalDate(today.year, today.monthNumber, 1)
        val startMillis = startDate.atStartOfDayIn(timeZone).toEpochMilliseconds()

        // End of the month: last day at 23:59:59.999
        val firstDayNextMonth = if (today.monthNumber == 12) LocalDate(today.year + 1, 1, 1)
        else LocalDate(today.year, today.monthNumber + 1, 1)
        val lastDayOfMonth = firstDayNextMonth.minus(1, DateTimeUnit.DAY)
        val endLocalTime = LocalTime(23, 59, 59, 999_000_000)
        val endMillis =
            lastDayOfMonth.atTime(endLocalTime).toInstant(timeZone).toEpochMilliseconds()

        return startMillis to endMillis
    }

}


