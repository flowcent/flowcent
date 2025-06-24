/*
 * Created by Saeedus Salehin on 24/5/25, 8:51 PM.
 */

package com.aiapp.flowcent.di

import com.aiapp.flowcent.core.platform.FlowCentAi
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { FlowCentAi() }
}