package com.aiapp.flowcent.core.utils

import com.aiapp.flowcent.core.domain.utils.Constants.UUID_PREFIX_FLOWCENT_USER
import com.aiapp.flowcent.core.domain.utils.Constants.UUID_PREFIX_TRANSACTION
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
    return accountID
}


fun getFlowCentUserId(phoneNumber: String): String {
    val uuid = "$UUID_PREFIX_FLOWCENT_USER$phoneNumber"
    return uuid
}