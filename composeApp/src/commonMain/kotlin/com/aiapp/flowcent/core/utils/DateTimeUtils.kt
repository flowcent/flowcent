/*
 * Created by Saeedus Salehin on 23/5/25, 10:50 PM.
 */

package com.aiapp.flowcent.core.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

object DateTimeUtils {
    fun getCurrentDate(): LocalDate =
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    fun daysInMonth(date: LocalDate): Int {
        val nextMonth = date.plus(1, DateTimeUnit.MONTH)
        return nextMonth.minus(1, DateTimeUnit.DAY).dayOfMonth
    }
}