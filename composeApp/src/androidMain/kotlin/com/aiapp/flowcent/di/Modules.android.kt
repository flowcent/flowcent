/*
 * Created by Saeedus Salehin on 24/5/25, 8:49 PM.
 */

package com.aiapp.flowcent.di

import com.aiapp.flowcent.core.datastore.createDataStore
import com.aiapp.flowcent.core.platform.AndroidPhoneVerificationProvider
import com.aiapp.flowcent.core.platform.FlowCentAi
import com.aiapp.flowcent.core.platform.phoneVerificationProvider
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { FlowCentAi() }
    single { createDataStore(context = get()) }
    single { AndroidPhoneVerificationProvider(get()) }
    single { phoneVerificationProvider() }
}