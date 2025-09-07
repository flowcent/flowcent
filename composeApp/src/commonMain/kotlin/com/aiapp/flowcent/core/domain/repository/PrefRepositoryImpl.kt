package com.aiapp.flowcent.core.domain.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.aiapp.flowcent.core.data.repository.PrefRepository
import com.aiapp.flowcent.core.domain.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

class PrefRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : PrefRepository {
    private object PreferencesKeys {
        val USER_UID = stringPreferencesKey(Constants.PREFERENCE_KEY_USER_ID)
        val KEY_LAST_PLAN_UPDATE_TIME = stringPreferencesKey(Constants.KEY_LAST_PLAN_UPDATE_TIME)
    }

    override val uid: Flow<String?> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_UID]
    }

    override suspend fun saveUid(uid: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_UID] = uid
        }
    }

    override suspend fun deleteUid() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.USER_UID)
        }
    }

    override suspend fun hasUpdatedLatestPlan() {
        val lastRunTime = dataStore.data.map { preferences ->
            preferences[PreferencesKeys.KEY_LAST_PLAN_UPDATE_TIME]
        }
    }

    override suspend fun updateLatestPlanUpdateTime() {
        TODO("Not yet implemented")
    }
}