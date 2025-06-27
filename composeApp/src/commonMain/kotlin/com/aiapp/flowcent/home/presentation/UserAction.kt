package com.aiapp.flowcent.home.presentation

sealed interface UserAction {
    data object FetchLatestTransactions: UserAction
}