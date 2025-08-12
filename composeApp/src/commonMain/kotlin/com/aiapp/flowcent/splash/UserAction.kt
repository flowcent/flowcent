package com.aiapp.flowcent.splash

sealed interface UserAction {
    data object FetchUidFromStore : UserAction
}