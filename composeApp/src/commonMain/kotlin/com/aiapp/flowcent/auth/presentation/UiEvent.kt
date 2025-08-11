package com.aiapp.flowcent.auth.presentation

sealed interface UiEvent {
    data object NavigateToHome : UiEvent
    data object NavigateToBasicIntro : UiEvent
    data object NavigateToSignIn : UiEvent
    data object NavigateToSignUp : UiEvent
}