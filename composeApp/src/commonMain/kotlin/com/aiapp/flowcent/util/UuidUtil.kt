package com.aiapp.flowcent.util

import com.aiapp.flowcent.util.Constants.UUID_PREFIX_TRANSACTION
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun getTransactionId(): String {
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

    val accountID = "$UUID_PREFIX_TRANSACTION-$timestamp"
    println("Generated Account ID: $accountID")
    return accountID
}