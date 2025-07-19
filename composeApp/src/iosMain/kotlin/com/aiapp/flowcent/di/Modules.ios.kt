/*
 * Created by Saeedus Salehin on 24/5/25, 8:51 PM.
 */

package com.aiapp.flowcent.di

import com.aiapp.flowcent.core.datastore.createDataStore
import com.aiapp.flowcent.core.datastore.dataStoreFileName
import com.aiapp.flowcent.core.platform.FlowCentAi
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

actual val platformModule: Module = module {
    single { FlowCentAi() }
    single {
        createDataStore(
            producePath = {
                // Get the standard documents directory path for iOS
                val documentsDirectory = NSSearchPathForDirectoriesInDomains(
                    directory = NSDocumentDirectory,
                    domainMask = NSUserDomainMask,
                    expandTilde = true
                ).first() as String

                // Append the file name to create the full path
                "$documentsDirectory/$dataStoreFileName"
            }
        )
    }
}