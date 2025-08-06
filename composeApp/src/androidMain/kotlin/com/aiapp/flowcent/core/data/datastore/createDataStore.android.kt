package com.aiapp.flowcent.core.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.aiapp.flowcent.core.data.local.datastore.createDataStore
import com.aiapp.flowcent.core.data.local.datastore.dataStoreFileName


fun createDataStore(context: Context): DataStore<Preferences> = createDataStore(
    producePath = { context.filesDir.resolve(dataStoreFileName).absolutePath }
)