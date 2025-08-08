package com.aiapp.flowcent.core.domain.utils

object Constants {
    const val SIGN_IN_TYPE_GOOGLE = "google"
    const val SIGN_IN_TYPE_APPLE = "apple"
    const val SIGN_IN_TYPE_EMAIL = "email"
    const val UUID_PREFIX_ACCOUNT = "FCAC"
    const val UUID_PREFIX_TRANSACTION = "FCTN"

}

object EnumConstants {
    enum class TransactionType {
        INCOME,
        EXPENSE,
        LEND,
        BORROW
    }
}