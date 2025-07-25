package com.aiapp.flowcent.core.permission

import dev.icerock.moko.permissions.PermissionState

data class FCPermissionState(
    val audioPermissionState: PermissionState? = null,
    val contactPermissionState: PermissionState? = null,
)