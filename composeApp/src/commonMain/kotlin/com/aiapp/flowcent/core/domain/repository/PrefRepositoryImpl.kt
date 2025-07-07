package com.aiapp.flowcent.core.domain.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.aiapp.flowcent.core.data.repository.PrefRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PrefRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : PrefRepository {
    private object PreferencesKeys {
        val USER_UID = stringPreferencesKey("user_uid")
    }

    override val uid: Flow<String?> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_UID]
    }

    override suspend fun saveUid(uid: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_UID] = uid
        }
    }
}