package com.aiapp.flowcent.onboarding

data class OnboardingState(
    val currentPage: Int = 0,
    val totalPages: Int = 3,
    val isSaving: Boolean = false
)

