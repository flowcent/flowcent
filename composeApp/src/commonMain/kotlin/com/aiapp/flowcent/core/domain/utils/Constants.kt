package com.aiapp.flowcent.core.domain.utils

import kotlinx.serialization.SerialName

object Constants {
    const val SIGN_IN_TYPE_GOOGLE = "google"
    const val SIGN_IN_TYPE_APPLE = "apple"
    const val SIGN_IN_TYPE_EMAILPASS = "emailpass"
    const val UUID_PREFIX_ACCOUNT = "FCAC"
    const val UUID_PREFIX_TRANSACTION = "FCTN"
    const val PREFERENCE_KEY_USER_ID = "user_uid"
    const val KEY_LAST_PLAN_UPDATE_TIME = "last_plan_update_time"
    const val UUID_PREFIX_FLOWCENT_USER =
        "FCUSER" // ***use in to generate revenuecat app user id ***//
}

object EnumConstants {
    enum class TransactionType {
        @SerialName("Income")
        INCOME,

        @SerialName("Expense")
        EXPENSE,

        @SerialName("Lend")
        LEND,

        @SerialName("Borrow")
        BORROW
    }
}