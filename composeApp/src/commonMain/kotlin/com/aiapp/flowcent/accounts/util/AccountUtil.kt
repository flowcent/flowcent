package com.aiapp.flowcent.accounts.util

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun getAccountID(): String {
    val prefix = "FCAC"
    val currentMoment = Clock.System.now()
    val localTime = currentMoment.toLocalDateTime(TimeZone.UTC)
    val milliseconds = currentMoment.toEpochMilliseconds() % 1000  // Get milliseconds

    val timestamp = buildString {
        append(localTime.year)
        append(localTime.monthNumber.toString().padStart(2, '0'))
        append(localTime.dayOfMonth.toString().padStart(2, '0'))
        append(localTime.hour.toString().padStart(2, '0'))
        append(localTime.minute.toString().padStart(2, '0'))
        append(localTime.second.toString().padStart(2, '0'))
        append(milliseconds.toString().padStart(3, '0'))  // Pad milliseconds to 3 digits
    }

    val accountID = "$prefix-$timestamp"
    println("Generated Account ID: $accountID")
    return accountID
}
