package com.aiapp.flowcent.core.data.repository

import kotlinx.coroutines.flow.Flow

interface PrefRepository {
    val uid: Flow<String?>

    suspend fun saveUid(uid: String)

    suspend fun deleteUid()

    suspend fun hasUpdatedLatestPlan()

    suspend fun updateLatestPlanUpdateTime()
}